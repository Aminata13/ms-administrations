package com.safelogisitics.gestionentreprisesusers.service;


import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.exception.TokenRefreshException;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.RefreshToken;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.payload.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.TokenRefreshRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.security.jwt.JwtUtils;

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
	RefreshTokenService refreshTokenService;

	@Autowired
  JwtUtils jwtUtils;

  @Override
  public JwtResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		User userDetails = validateCompteUser(loginRequest.getUsername(), loginRequest.getNumeroEmei());
    if(userDetails == null) {
      return null;
    }

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		return new JwtResponse(jwt, refreshToken.getToken());
  }

  @Override
  public JwtResponse refreshToken(TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
      .map(refreshTokenService::verifyExpiration)
      .map(RefreshToken::getUser)
      .map(user -> {
        User userDetails = validateCompteUser(user.getUsername(), request.getNumeroEmei());
        if(userDetails == null) {
          return null;
        }

        String token = jwtUtils.generateJwtTokenFromUser(user);
        return new JwtResponse(token, requestRefreshToken);
      })
      .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
        "Refresh token is not not found!"));
  }

  @Override
  public User validateCompteUser(String username, String numeroEmei) {
    Optional<User> userExist = userDao.findByUsername(username);

    if(!userExist.isPresent() || userExist.get().getStatut() == -1) {
      return null;
    }

    User user = userExist.get();

    for (Compte compte : user.getInfosPerso().getComptes()) {
      if (compte.isDeleted() || (numeroEmei != null && !compte.getType().equals(ECompteType.COMPTE_COURSIER))) {
        continue;
      }

      if (
        (numeroEmei == null && compte.getType().equals(ECompteType.COMPTE_COURSIER)) ||
        (numeroEmei != null && compte.getType().equals(ECompteType.COMPTE_COURSIER) && (compte.getNumeroEmei().isEmpty() || !compte.getNumeroEmei().equals(numeroEmei)))
      ) {
        break;
      }

      return user;
    }

    return null;
  }
}