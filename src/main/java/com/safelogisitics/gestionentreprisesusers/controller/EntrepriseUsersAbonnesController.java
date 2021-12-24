package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.EProspecteurType;
import com.safelogisitics.gestionentreprisesusers.payload.request.ProspectRequest;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;
import com.safelogisitics.gestionentreprisesusers.service.ProspectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/entreprises-users-abonnees")
@PreAuthorize("hasRole('COMPTE_ENTREPRISE')")
@Api(tags = "Gestion des utilisateurs abonnées entreprise", description = "Api entreprise: gestion des utilisateurs abonnées")
public class EntrepriseUsersAbonnesController {
  
  @Autowired
  private ProspectService prospectService;

  @Autowired
  private CompteDao compteDao;

  @ApiOperation(value = "Liste des utilisateurs abonnées")
  @GetMapping("/list")
	public ResponseEntity<?> allUsersAbonnees(@RequestParam Map<String,String> parameters, Pageable pageable) {
    
    Compte compte = validateUserAccount();
    if (compte == null)
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accès refusé");

    if (parameters == null)
      parameters = new HashMap<>();

    parameters.put("prospecteurId", compte.getEntrepriseId());
    parameters.put("prospecteurType", EProspecteurType.COMPTE_ENTREPRISE.name());

    return ResponseEntity.status(HttpStatus.OK).body(prospectService.getProspects(parameters, pageable));
	}

  @ApiOperation(value = "Affichage d'un utilisateur abonnée")
  @GetMapping("/{id}")
	public ResponseEntity<?> oneUserAbonnee(@PathVariable(value = "id") String id) {
    Compte compte = validateUserAccount();
    if (compte == null)
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accès refusé");

    Map<String,String>parameters = new HashMap<>();
    parameters.put("prospecteurId", compte.getEntrepriseId());
    parameters.put("prospecteurType", EProspecteurType.COMPTE_ENTREPRISE.name());
    
    return ResponseEntity.status(HttpStatus.OK).body(prospectService.getProspects(parameters,
      PageRequest.of(0, 1)).get().findFirst());
	}

  @ApiOperation(value = "Création d'un nouveau utilisateur abonnée")
  @PostMapping("/add")
	public ResponseEntity<Map<String, Object>> addUserAbonnee(@Valid @RequestBody ProspectRequest request) {
    Compte compte = validateUserAccount();
    if (compte == null)
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accès refusé");

    Map<String, Object> prospect = prospectService.createProspect(request, EProspecteurType.COMPTE_ENTREPRISE);
		return ResponseEntity.status(HttpStatus.CREATED).body(prospect);
	}

  @ApiOperation(value = "Mise à jour d'un utilisateur abonnée")
  @PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateUserAbonnee(@PathVariable(value = "id") String id, @Valid @RequestBody ProspectRequest request) {
    Compte compte = validateUserAccount();
    if (compte == null)
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accès refusé");

    Map<String, Object> prospect = prospectService.updateProspect(id, request, false);

		return ResponseEntity.status(HttpStatus.OK).body(prospect);
	}

  @ApiOperation(value = "Suppression d'un utilisateur abonnée")
  @DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUserAbonnee(@PathVariable(value = "id") String id) {
    prospectService.deleteProspect(id);
    return ResponseEntity.status(HttpStatus.OK).body("OK!");
	}

  private Compte validateUserAccount() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId()))
      return null;

    return compte;
  }
}
