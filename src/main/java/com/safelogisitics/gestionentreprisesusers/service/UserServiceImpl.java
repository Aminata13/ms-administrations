package com.safelogisitics.gestionentreprisesusers.service;


import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.LoginRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.TokenRefreshRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.response.JwtResponse;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.RefreshToken;
import com.safelogisitics.gestionentreprisesusers.data.model.User;
import com.safelogisitics.gestionentreprisesusers.web.exception.TokenRefreshException;
import com.safelogisitics.gestionentreprisesusers.web.security.jwt.JwtUtils;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

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

		User userDetails = validateCompteUser(loginRequest.getUsername(), loginRequest.getNumeroEmei());
    if(userDetails == null) {
      return null;
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		return new JwtResponse(jwt, refreshToken.getToken());
  }

  @Override
  public void logout() {
    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Optional<User> userExist = userDao.findByInfosPersoId(currentUser.getInfosPerso().getId());
    if (!userExist.isPresent()) {
      return;
    }

    User user = userExist.get();
    // Blacklist old accessToken if exist
    jwtUtils.blacklistAccesstoken(user.getCurrentAccessToken());
    user.setAuthenticated(false);
    user.setCurrentAccessToken(null);
    userDao.save(user);

    refreshTokenService.deleteByUserId(user.getId());
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

        user.setAuthenticated(false);
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
      boolean enroleur = compte.getType().equals(ECompteType.COMPTE_ADMINISTRATEUR) && compte.getRole() != null && compte.getRole().hasPrivilegeAction("GESTION_ABONNEMENTS", "CREATE");

      if (compte.isDeleted() || (numeroEmei != null && !numeroEmei.isEmpty() && !compte.getType().equals(ECompteType.COMPTE_COURSIER) && !enroleur )) {
        continue;
      }

      if (
        (numeroEmei == null && compte.getType().equals(ECompteType.COMPTE_COURSIER)) ||
        (numeroEmei != null && !numeroEmei.isEmpty() && compte.getType().equals(ECompteType.COMPTE_COURSIER) && (compte.getNumeroEmei() == null || compte.getNumeroEmei().isEmpty() || !compte.getNumeroEmei().equals(numeroEmei)))
      ) {
        break;
      }

      return user;
    }

    return null;
  }
}