package com.safelogisitics.gestionentreprisesusers.web.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.CommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.CommissionService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.UserService;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@Api(tags = "Api gestion infos perso", description = "Api gestion infos perso")
public class UserController {

	@Autowired
	UserService userService;

  @Autowired
	InfosPersoService infosPersoService;

  @Autowired
	AbonnementService abonnementService;

	@Autowired
  private CommissionService commissionService;

	@Autowired
  private CompteDao compteDao;

  @GetMapping("/infos")
	public ResponseEntity<?> infos() {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.getUserInfos());
  }

  @GetMapping("/infos/entreprise")
	public ResponseEntity<?> infosEntreprise() {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.getEntrepriseInfos());
  }

  @PostMapping("/infos")
	public ResponseEntity<?> updateInfos(@Valid @RequestBody UpdateInfosPersoRequest updateInfosPersoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.updateUserInfos(updateInfosPersoRequest));
  }

	@GetMapping("/infos/{id}")
	public ResponseEntity<?> infosById(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findInfosPersoByCompteId(id));
  }

  @GetMapping(value = {"/administrateur/commissions", "/coursier/commissions"})
	public ResponseEntity<Page<CommissionModel>> getMyCommissions(@Valid CommissionSearchRequestDto commissionSearchRequest, Pageable pageable, HttpServletRequest request) {
		String stringCompte = String.format("COMPTE_%s", Arrays.asList(request.getRequestURI().split("[\\/]")).get(5).toUpperCase());
    ECompteType compteType = ECompteType.valueOf(stringCompte);

		UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Compte> compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), compteType);

		if (!compte.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(Page.empty());
		}

		commissionSearchRequest.setResponsableId(compte.get().getId());

		Page<CommissionModel> commissions = commissionService.getListCommissions(commissionSearchRequest, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(commissions);
  }

	@GetMapping(value = {"/administrateur/paiements/commissions", "/coursier/paiements/commissions"})
	public ResponseEntity<Page<PaiementCommissionModel>> getPaiementsCommissions(@Valid CommissionSearchRequestDto commissionSearchRequest, Pageable pageable, HttpServletRequest request) {
		String stringCompte = String.format("COMPTE_%s", Arrays.asList(request.getRequestURI().split("[\\/]")).get(5).toUpperCase());
    ECompteType compteType = ECompteType.valueOf(stringCompte);

		UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Compte> compte = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), compteType);

		if (!compte.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(Page.empty());
		}

		commissionSearchRequest.setResponsableId(compte.get().getId());

    Page<PaiementCommissionModel> paiements = commissionService.getListPaiementCommissions(commissionSearchRequest, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(paiements);
  }

  @GetMapping("/infos/multiple")
	public ResponseEntity<?> multipleInfosByIds(@RequestParam(required = true) Set<String> ids) {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findAllInfosPersoByCompteIds(ids));
  }

  @PostAuthorize("hasRole('COMPTE_COURSIER')")
  @GetMapping("/client/abonnement/{numeroCarte}")
	public ResponseEntity<?> getByNumeroCarte(@PathVariable(value = "numeroCarte") String numeroCarte) {
		return ResponseEntity.status(HttpStatus.OK).body(abonnementService.getCustomResponseByNumeroCarte(numeroCarte));
  }

  @GetMapping("/infos/search/{emailOrTelephone}")
	public ResponseEntity<?> getByEmailOrTelephone(@PathVariable(value = "emailOrTelephone") String emailOrTelephone) {
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findByEmailOrTelephone(emailOrTelephone));
	}

  @GetMapping("/verifier/abonnement/{telephone}")
	public ResponseEntity<?> verifierAbonnement(@PathVariable(value = "telephone") String telephone) {
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.verifierAbonnement(telephone));
	}
}
