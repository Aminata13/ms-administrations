package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Collections;
import java.util.Set;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.model.AffectationEquipement;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.EServiceConciergeType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/profils")
@PreAuthorize("hasRole('COMPTE_ADMINISTRATEUR')")
@Api(tags = "profils", description = "Api gestion des personnels et agents")
public class ProfilController {

  @Autowired
  private InfosPersoService infosPersoService;

  @ApiOperation(value = "", tags = "profils")
  @GetMapping("/search/{emailOrTelephone}")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'READ')")
	public ResponseEntity<?> getByEmailOrTelephone(@PathVariable(value = "emailOrTelephone") String emailOrTelephone) {
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findByEmailOrTelephone(emailOrTelephone));
	}

  @GetMapping("/custom-search")
	public ResponseEntity<?> getByCustomSearch(
    @RequestParam(required = false) String prenom,
    @RequestParam(required = false) String nom,
    @RequestParam(required = false) String email,
    @RequestParam(required = false) String telephone,
    @RequestParam(required = false) String numeroCarte,
    @RequestParam(required = false) ECompteType compteType,
    @RequestParam(required = false) EServiceConciergeType serviceConciergeType
  ) {

    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findByCustomSearch(prenom, nom, email, telephone, numeroCarte, compteType, serviceConciergeType));
	}

  @ApiOperation(value = "", tags = "personnels")
  @GetMapping("/get-by-compte/{id}")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'READ') or hasPermission('GESTION_AGENTS', 'READ')")
	public ResponseEntity<?> getByCompte(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPersoService.findByCompteId(id));
	}

  @ApiOperation(value = "", tags = "personnels")
  @GetMapping("/personnels/list")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'READ')")
	public ResponseEntity<?> allPersonnels(@PageableDefault(size = 20) Pageable pageable) {
    Page<UserInfosResponse> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_ADMINISTRATEUR, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "personnels")
  @PostMapping("/personnels/add")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'CREATE')")
	public ResponseEntity<?> addPersonnel(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "personnels")
  @PutMapping("/personnels/update/{id}")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'UPDATE')")
	public ResponseEntity<?> updatePersonnel(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAdministrateur(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "personnels")
  @DeleteMapping("/personnels/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_PERSONNELS', 'DELETE')")
	public ResponseEntity<?> deletePersonnel(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personnel with that id does not exists!");

    infosPersoService.deleteCompteAdministrateur(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "supprimé"));
	}

  @ApiOperation(value = "", tags = "agents")
  @GetMapping("/agents/list")
  @PreAuthorize("hasPermission('GESTION_AGENTS', 'READ')")
	public ResponseEntity<?> allAgents(@PageableDefault(size = 20) Pageable pageable) {
    Page<UserInfosResponse> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_COURSIER, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "agents")
  @PostMapping("/agents/add")
  @PreAuthorize("hasPermission('GESTION_AGENTS', 'CREATE')")
	public ResponseEntity<?> addAgent(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAgent(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "agents")
  @PutMapping("/agents/update/{id}")
  @PreAuthorize("hasPermission('GESTION_AGENTS', 'UPDATE')")
	public ResponseEntity<?> updateAgent(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateCompteAgent(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "agents")
  @PutMapping("/agents/equiper/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'ASSIGN')")
	public ResponseEntity<?> equiperAgent(@PathVariable(value = "id") String id, @Valid @RequestBody Set<AffectationEquipement> request) {
    InfosPerso infosPerso = infosPersoService.equiperAgent(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "agents")
  @PutMapping("/agents/affecter-moyen-transport/{id}")
  @PreAuthorize("hasPermission('GESTION_MATERIELS', 'ASSIGN')")
	public ResponseEntity<?> affecterMoyenTransportAgent(@PathVariable(value = "id") String id, @Valid @RequestBody String moyenTransportId) {
    InfosPerso infosPerso = infosPersoService.affecterMoyenTransportAgent(id, moyenTransportId);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "Suppression d'un agent", tags = "agents")
  @DeleteMapping("/agents/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_AGENTS', 'DELETE')")
	public ResponseEntity<?> deleteAgent(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agent with that id does not exists!");

    infosPersoService.deleteCompteAgent(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "supprimé"));
	}

  @ApiOperation(value = "", tags = "prestataires")
  @GetMapping("/prestataires/list")
  @PreAuthorize("hasPermission('GESTION_PRESTATAIRES', 'READ')")
	public ResponseEntity<?> allPrestataires(@PageableDefault(size = 20) Pageable pageable) {
    Page<UserInfosResponse> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_PRESTATAIRE, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "prestataires")
  @PostMapping("/prestataires/add")
  @PreAuthorize("hasPermission('GESTION_PRESTATAIRES', 'CREATE')")
	public ResponseEntity<?> addPrestataire(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateComptePrestataire(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "prestataires")
  @PutMapping("/prestataires/update/{id}")
  @PreAuthorize("hasPermission('GESTION_PRESTATAIRES', 'UPDATE')")
	public ResponseEntity<?> updatePrestataire(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    InfosPerso infosPerso = infosPersoService.createOrUpdateComptePrestataire(id, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "Suppression d'un prestataire", tags = "prestataires")
  @DeleteMapping("/prestataires/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_PRESTATAIRES', 'DELETE')")
	public ResponseEntity<?> deleteprestataire(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "prestataire with that id does not exists!");

    infosPersoService.deleteComptePrestataire(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "supprimé"));
	}

  @ApiOperation(value = "", tags = "clients")
  @GetMapping("/clients/list")
  @PreAuthorize("hasPermission('GESTION_CLIENTS', 'READ')")
	public ResponseEntity<?> allClients(
    @RequestParam(required = false) Boolean isAbonnee,
    @PageableDefault(size = 20) Pageable pageable) {
    Page<UserInfosResponse> roles = infosPersoService.getInfosPersos(ECompteType.COMPTE_PARTICULIER, isAbonnee, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
	}

  @ApiOperation(value = "", tags = "clients")
  @GetMapping("/clients/{id}")
  @PreAuthorize("hasPermission('GESTION_CLIENTS', 'READ')")
	public ResponseEntity<?> getClient(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with that id does not exists!");

		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.getCompteClient(id));
	}

  @ApiOperation(value = "", tags = "clients")
  @PostMapping("/clients/add")
  @PreAuthorize("hasPermission('GESTION_CLIENTS', 'CREATE')")
	public ResponseEntity<?> addClient(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPersoService.createCompteClient(request));
	}

  @ApiOperation(value = "", tags = "clients")
  @PutMapping("/clients/update/{id}")
  @PreAuthorize("hasPermission('GESTION_CLIENTS', 'UPDATE')")
	public ResponseEntity<?> updateClient(@PathVariable(value = "id") String id, @Valid @RequestBody UpdateInfosPersoRequest request) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with that id does not exists!");

    request.setPassword(null);

		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.updateCompteClient(id, request));
	}

  @ApiOperation(value = "Suppression d'un Client", tags = "clients")
  @DeleteMapping("/clients/delete/{id}")
  @PreAuthorize("hasPermission('GESTION_CLIENTS', 'DELETE')")
	public ResponseEntity<?> deleteClient(@PathVariable(value = "id") String id) {
    if (!infosPersoService.findInfosPersoById(id).isPresent())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client with that id does not exists!");

    infosPersoService.deleteCompteClient(id);
		return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "supprimé"));
	}
}