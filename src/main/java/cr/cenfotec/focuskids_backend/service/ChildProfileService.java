package cr.cenfotec.focuskids_backend.service;

import cr.cenfotec.focuskids_backend.dto.ChildProfileDTO;
import cr.cenfotec.focuskids_backend.dto.ChildProfileRequest;
import cr.cenfotec.focuskids_backend.model.ChildProfile;
import cr.cenfotec.focuskids_backend.repository.ChildProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

// Aqui va la logica principal de la gestion de perfiles de ninos
@Service
@RequiredArgsConstructor
public class ChildProfileService {

    private final ChildProfileRepository childProfileRepository;

    // Trae todos los perfiles que pertenecen a un padre
    public List<ChildProfileDTO> getAllProfilesByParent(Long parentUserId) {
        return childProfileRepository
                .findByParentUserIdOrderByNameAsc(parentUserId)
                .stream().map(ChildProfileDTO::fromEntity).collect(Collectors.toList());
    }

    // Cambia el perfil activo sin cerrar la sesion del padre
    public ChildProfileDTO switchProfile(Long profileId, Long parentUserId) {
        ChildProfile profile = childProfileRepository
                .findByIdAndParentUserId(profileId, parentUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        if (!profile.isActive())
            throw new RuntimeException("Este perfil esta desactivado.");
        return ChildProfileDTO.fromEntity(profile);
    }

    // Crea un nuevo perfil para un nino
    @Transactional
    public ChildProfileDTO createProfile(ChildProfileRequest request, Long parentUserId) {
        // Verifica que no exista otro perfil con el mismo nombre para este padre
        if (childProfileRepository.existsByNameAndParentUserId(request.getName(), parentUserId))
            throw new RuntimeException("Ya existe un perfil con ese nombre");
        ChildProfile p = new ChildProfile();
        p.setName(request.getName());
        p.setAvatar(request.getAvatar() != null ? request.getAvatar() : "fox");
        p.setEdad(request.getEdad());
        p.setCondicion(request.getCondicion());
        p.setActive(true);
        p.setParentUserId(parentUserId);
        return ChildProfileDTO.fromEntity(childProfileRepository.save(p));
    }

    // Edita el nombre, avatar, edad o condicion de un perfil existente
    @Transactional
    public ChildProfileDTO updateProfile(Long profileId, ChildProfileRequest request, Long parentUserId) {
        ChildProfile p = childProfileRepository
                .findByIdAndParentUserId(profileId, parentUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        p.setName(request.getName());
        if (request.getAvatar() != null) p.setAvatar(request.getAvatar());
        p.setEdad(request.getEdad());
        p.setCondicion(request.getCondicion());
        return ChildProfileDTO.fromEntity(childProfileRepository.save(p));
    }

    // Activa o desactiva un perfil. Si se desactiva, el historial de juegos se conserva
    @Transactional
    public ChildProfileDTO toggleProfileStatus(Long profileId, Long parentUserId) {
        ChildProfile p = childProfileRepository
                .findByIdAndParentUserId(profileId, parentUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        p.setActive(!p.isActive());
        return ChildProfileDTO.fromEntity(childProfileRepository.save(p));
    }

    // Elimina un perfil permanentemente. El frontend pide confirmacion antes de llamar esto
    @Transactional
    public void deleteProfile(Long profileId, Long parentUserId) {
        ChildProfile p = childProfileRepository
                .findByIdAndParentUserId(profileId, parentUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        childProfileRepository.delete(p);
    }
}
