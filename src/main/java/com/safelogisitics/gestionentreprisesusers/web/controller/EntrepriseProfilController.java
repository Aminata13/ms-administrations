package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/entreprise/personnels")
@PreAuthorize("hasRole('COMPTE_ENTREPRISE')")
@Api(tags = "Entreprise personnels", description = "Api gestion des personnels d'une entreprise")
public class EntrepriseProfilController {

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private CompteDao compteDao;

  @ApiOperation(value = "", tags = "Entreprise personnels")
  @GetMapping("/get/{id}")
	public ResponseEntity<?> getByCompte(@PathVariable(value = "id") String id) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();

    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId())
    || !compteDao.existsByInfosPersoIdAndEntrepriseId(id, compte.getEntreprise().getId()))
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Accès refusé"));

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(infosPersoService.getUserInfos(id));
	}

  @ApiOperation(value = "", tags = "Entreprise personnels")
  @GetMapping("/list")
	public ResponseEntity<?> allPersonnels(@RequestParam Map<String,String> parameters, @PageableDefault(size = 20) Pageable pageable) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId()))
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Accès refusé"));

    Page<UserInfosResponse> users = infosPersoService.getEntrepriseUsers(compte.getEntreprise().getId(), parameters, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(users);
	}

  @ApiOperation(value = "", tags = "Entreprise personnels")
  @PostMapping("/add")
	public ResponseEntity<?> addPersonnel(@Valid @RequestBody InfosPersoAvecCompteRequest request) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId()))
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Accès refusé"));

    request.setEntrepriseId(compte.getEntreprise().getId());
    InfosPersoModel infosPerso = infosPersoService.createOrUpdateCompteEntreprise(null, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "Entreprise personnels")
  @PutMapping("/update/{id}")
	public ResponseEntity<?> updatePersonnel(@PathVariable(value = "id") String id, @Valid @RequestBody InfosPersoAvecCompteRequest request) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId()))
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Accès refusé"));

    request.setEntrepriseId(compte.getEntreprise().getId());
    InfosPersoModel infosPerso = infosPersoService.createOrUpdateCompteEntreprise(id, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(infosPerso);
	}

  @ApiOperation(value = "", tags = "Entreprise personnels")
  @DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePersonnel(@PathVariable(value = "id") String id) {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ENTREPRISE).get();
    if (compte.isDeleted() || !compte.isEntrepriseUser() || !compte.getEntreprise().getGerantId().equals(compte.getId())
    || !compteDao.existsByInfosPersoIdAndEntrepriseId(id, compte.getEntreprise().getId()))
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Accès refusé"));

    infosPersoService.deleteCompteEntreprise(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "DELETED"));
	}
}
