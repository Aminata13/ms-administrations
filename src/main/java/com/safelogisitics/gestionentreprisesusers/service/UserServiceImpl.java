package com.safelogisitics.gestionentreprisesusers.service;


import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.security.jwt.JwtUtils;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDao userDao;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
  JwtUtils jwtUtils;
  
  @Override
  public JwtResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    
    if(!userDetails.isAccountNonLocked() || !userDetails.isEnabled()) {
      return null;
    }

		return new JwtResponse(jwt);
  }
}