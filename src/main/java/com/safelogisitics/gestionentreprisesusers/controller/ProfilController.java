package com.safelogisitics.gestionentreprisesusers.controller;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/profils")
public class ProfilController {

  @Autowired
  private InfosPersoService infosPersoService;

  @GetMapping("/personnels/list")
  @PreAuthorize("hasAuthority('COMPTE_ADMINISTRATEUR') and hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allPersonnels(@PageableDefault(size = 20) Pageable pageable) {
    Page<InfosPerso> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_ADMINISTRATEUR, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @PostMapping("/personnels/add")
  @PreAuthorize("hasAuthority('COMPTE_ADMINISTRATEUR') and hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addPersonnel(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @PutMapping("/personnels/update/{id}")
  @PreAuthorize("hasAuthority('COMPTE_ADMINISTRATEUR') and hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updatePersonnel(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personnel with that id does not exists!");

    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @DeleteMapping("/personnels/delete/{id}")
  @PreAuthorize("hasAuthority('COMPTE_ADMINISTRATEUR') and hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deletePersonnel(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personnel with that id does not exists!");

    infosPersoService.deleteCompteAdministrateur(id);
		return ResponseEntity.status(HttpStatus.CREATED).body("DELETED");
	}
}
