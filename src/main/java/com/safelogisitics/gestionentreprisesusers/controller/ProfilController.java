package com.safelogisitics.gestionentreprisesusers.controller;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/profils")
@PostAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "profils", description = "Api gestion des personnels et agents")
public class ProfilController {

  @Autowired
  private InfosPersoService infosPersoService;

  @ApiOperation(value = "", tags = "profils")
  @GetMapping("/search/{emailOrTelephone}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> getByEmailOrTelephone(@PathVariable(value = "emailOrTelephone") String emailOrTelephone) {
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findByEmailOrTelephone(emailOrTelephone, emailOrTelephone));
	}

  @ApiOperation(value = "", tags = "personnels")
  @GetMapping("/personnels/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allPersonnels(@PageableDefault(size = 20) Pageable pageable) {
    Page<InfosPerso> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_ADMINISTRATEUR, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "personnels")
  @PostMapping("/personnels/add")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addPersonnel(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "personnels")
  @PutMapping("/personnels/update/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updatePersonnel(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "personnels")
  @DeleteMapping("/personnels/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deletePersonnel(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personnel with that id does not exists!");

    infosPersoService.deleteCompteAdministrateur(id);
		return ResponseEntity.status(HttpStatus.CREATED).body("DELETED");
	}

  @ApiOperation(value = "", tags = "agents")
  @GetMapping("/agents/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allAgents(@PageableDefault(size = 20) Pageable pageable) {
    Page<InfosPerso> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_COURSIER, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "agents")
  @PostMapping("/agents/add")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addAgent(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAgent(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "agents")
  @PutMapping("/agents/update/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updateAgent(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAgent(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "Suppression d'un agent", tags = "agents")
  @DeleteMapping("/agents/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deleteAgent(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent with that id does not exists!");

    infosPersoService.deleteCompteAgent(id);
		return ResponseEntity.status(HttpStatus.CREATED).body("DELETED");
	}

  @ApiOperation(value = "", tags = "prestataires")
  @GetMapping("/prestataires/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allPrestataires(@PageableDefault(size = 20) Pageable pageable) {
    Page<InfosPerso> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_PRESTATAIRE, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "prestataires")
  @PostMapping("/prestataires/add")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addPrestataire(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateComptePrestataire(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "prestataires")
  @PutMapping("/prestataires/update/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updatePrestataire(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateComptePrestataire(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "Suppression d'un prestataire", tags = "prestataires")
  @DeleteMapping("/prestataires/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deleteprestataire(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "prestataire with that id does not exists!");

    infosPersoService.deleteComptePrestataire(id);
		return ResponseEntity.status(HttpStatus.CREATED).body("DELETED");
	}

  @ApiOperation(value = "", tags = "clients")
  @GetMapping("/clients/list")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'READ')")
	public ResponseEntity<?> allClients(@PageableDefault(size = 20) Pageable pageable) {
    Page<InfosPerso> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_PARTICULIER, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "clients")
  @PostMapping("/clients/add")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'CREATE')")
	public ResponseEntity<?> addClient(@Valid @RequestBody RegisterRequest request) {
    InfosPerso infosPerso = infosPersoService.createCompteClient(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "clients")
  @PutMapping("/clients/update/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'WRITE')")
	public ResponseEntity<?> updateClient(@PathVariable(value = "id") String id, @Valid @RequestBody UpdateInfosPersoRequest request) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with that id does not exists!");

    InfosPerso infosPerso = infosPersoService.updateCompteClient(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(infosPerso);
	}

  @ApiOperation(value = "Suppression d'un Client", tags = "clients")
  @DeleteMapping("/clients/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_UTILISATEURS', 'DELETE')")
	public ResponseEntity<?> deleteClient(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with that id does not exists!");

    infosPersoService.deleteCompteClient(id);
		return ResponseEntity.status(HttpStatus.OK).body("DELETED");
	}
}
