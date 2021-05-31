package com.safelogisitics.gestionentreprisesusers.controller;

import javax.validation.Valid;

import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.TokenRefreshRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.UpdateInfosPersoRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.service.InfosPersoService;
import com.safelogisitics.gestionentreprisesusers.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/auth")
@Api(tags = "authentification & registration", description = "Api authentification & registration")
public class AuthController {

	@Autowired
	UserService userService;

  @Autowired
	InfosPersoService infosPersoService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		JwtResponse jwtRes = userService.authenticate(loginRequest);

		if(jwtRes == null)
			throw new RuntimeException("Error: Compte désactivé ou supprimé.");

		return ResponseEntity.ok(jwtRes);
	}

  @PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

		JwtResponse jwtRes = infosPersoService.clientRegistration(registerRequest);

		if(jwtRes == null)
			throw new RuntimeException("Error: Inscription non validé.");

		return ResponseEntity.ok(jwtRes);
	}

  @PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    JwtResponse jwtRes = userService.refreshToken(request);
		return ResponseEntity.ok(jwtRes);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/userinfos")
	public ResponseEntity<?> userInfos() {
		return ResponseEntity.ok(infosPersoService.getUserInfos());
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/userinfos")
	public ResponseEntity<?> updateUserInfos(@Valid @RequestBody UpdateInfosPersoRequest updateInfosPersoRequest) {
		return ResponseEntity.ok(infosPersoService.updateUserInfos(updateInfosPersoRequest));
  }
}
