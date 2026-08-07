#!/bin/bash
# =============================================================================
# CA-01: Renovación automática del certificado Let's Encrypt para FocusKids
#
# Este script es invocado por cron. certbot-setup.sh ya lo registra en el
# crontab del root; no hace falta instalarlo manualmente.
#
# Crontab generado por certbot-setup.sh:
#   0 3 * * * /ruta/al/deploy/renovar-ssl.sh
#
# Let's Encrypt renueva el certificado automáticamente cuando quedan < 30 días
# de validez (el certificado dura 90 días). Correr el script a diario es
# suficiente; si no hay nada que renovar termina en < 1 segundo.
# =============================================================================
set -euo pipefail

LOG="/var/log/focuskids-ssl-renew.log"
FECHA=$(date '+%Y-%m-%d %H:%M:%S')

echo "[$FECHA] Iniciando renovación SSL..." | tee -a "$LOG"

# Renovar certificado si quedan < 30 días
certbot renew \
    --quiet \
    --deploy-hook "systemctl reload nginx" \
    2>&1 | tee -a "$LOG"

# Verificar que nginx sigue levantado después de recargar
if systemctl is-active --quiet nginx; then
    echo "[$FECHA] ✅ Nginx activo. Renovación completada." | tee -a "$LOG"
else
    echo "[$FECHA] ❌ Nginx no está activo — revisión necesaria." | tee -a "$LOG"
    systemctl start nginx
    echo "[$FECHA] 🔄 Nginx reiniciado." | tee -a "$LOG"
fi

echo "[$FECHA] --- Fin del proceso de renovación ---" | tee -a "$LOG"
