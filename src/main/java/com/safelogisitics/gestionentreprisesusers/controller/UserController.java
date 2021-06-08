package com.safelogisitics.gestionentreprisesusers.controller;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.payload.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @GetMapping("/infos")
	public ResponseEntity<?> infos() {
		return ResponseEntity.ok(infosPersoService.getUserInfos());
  }

  @GetMapping("/infos/{id}")
	public ResponseEntity<?> infosById(@PathVariable(value = "id") String id) {
		return ResponseEntity.ok(infosPersoService.findInfosPersoByCompteId(id));
  }

  @PostMapping("/infos")
	public ResponseEntity<?> updateInfos(@Valid @RequestBody UpdateInfosPersoRequest updateInfosPersoRequest) {
		return ResponseEntity.ok(infosPersoService.updateUserInfos(updateInfosPersoRequest));
  }
}
