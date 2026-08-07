#!/bin/bash
# =============================================================================
# CA-01: Instalar Certbot y obtener certificado Let's Encrypt para FocusKids
# Ejecutar como root en Ubuntu 22.04 con nginx instalado y dominio apuntando
# al servidor.
#
# Uso: sudo bash certbot-setup.sh focuskids.example.com admin@example.com
# =============================================================================
set -euo pipefail

DOMINIO="${1:?'Uso: certbot-setup.sh <dominio> <email>'}"
EMAIL="${2:?'Uso: certbot-setup.sh <dominio> <email>'}"

echo "🔐 Instalando Certbot..."
apt-get update -q
apt-get install -y certbot python3-certbot-nginx

echo "📋 Copiando configuración de Nginx..."
cp "$(dirname "$0")/nginx.conf" /etc/nginx/sites-available/focuskids
# Reemplazar dominio placeholder por el dominio real
sed -i "s/focuskids.example.com/$DOMINIO/g" /etc/nginx/sites-available/focuskids

# Habilitar el sitio
ln -sf /etc/nginx/sites-available/focuskids /etc/nginx/sites-enabled/focuskids
rm -f /etc/nginx/sites-enabled/default

echo "🔍 Verificando configuración de Nginx..."
nginx -t

echo "🔄 Reiniciando Nginx (solo HTTP por ahora para que Certbot pueda validar)..."
systemctl reload nginx

echo "📜 Solicitando certificado a Let's Encrypt..."
certbot --nginx \
    --non-interactive \
    --agree-tos \
    --email "$EMAIL" \
    -d "$DOMINIO" \
    --redirect              # CA-03: activa la redirección HTTP→HTTPS automáticamente

echo "✅ Verificando que la renovación automática funciona..."
certbot renew --dry-run

# ── CA-01: configurar cron de renovación automática ──────────────────────────
CRON_LINE="0 3 * * * certbot renew --quiet --deploy-hook 'systemctl reload nginx'"
( crontab -l 2>/dev/null | grep -v certbot; echo "$CRON_LINE" ) | crontab -

echo ""
echo "============================================================"
echo "✅ HTTPS configurado correctamente para $DOMINIO"
echo "   Certificado: /etc/letsencrypt/live/$DOMINIO/"
echo "   Renovación:  automática vía cron (cada vez que queden < 30 días)"
echo "   Verificar:   https://www.ssllabs.com/ssltest/analyze.html?d=$DOMINIO"
echo "============================================================"
