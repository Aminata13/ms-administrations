package com.safelogisitics.gestionentreprisesusers.controller;

import java.util.Set;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.payload.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.service.AbonnementService;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/infos")
	public ResponseEntity<?> infos() {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.getUserInfos());
  }

  @PostMapping("/infos")
	public ResponseEntity<?> updateInfos(@Valid @RequestBody UpdateInfosPersoRequest updateInfosPersoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.updateUserInfos(updateInfosPersoRequest));
  }

  @GetMapping("/infos/{id}")
	public ResponseEntity<?> infosById(@PathVariable(value = "id") String id) {
		return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findInfosPersoByCompteId(id));
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
    return ResponseEntity.status(HttpStatus.OK).body(infosPersoService.findByEmailOrTelephone(emailOrTelephone, emailOrTelephone));
	}
}
