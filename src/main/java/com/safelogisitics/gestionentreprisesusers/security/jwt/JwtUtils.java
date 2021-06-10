package com.safelogisitics.gestionentreprisesusers.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.safelogisitics.gestionentreprisesusers.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.model.User;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  private final JwtConfig jwtConfig;

  public JwtUtils(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Autowired
  private UserDao userDao;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    User user = userDao.findById(userPrincipal.getId()).get();

    user.setAuthenticated(false);

		return generateJwtTokenFromUser(user);
	}

  public String generateJwtTokenFromUser(User user) {
    if (user.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Vous êtes déjà connecté");
    }

    String newAccessToken = Jwts.builder()
      .setSubject((user.getUsername()))
      .claim("id", user.getInfosPerso().getId())
      .claim("username", user.getUsername())
      .claim("infosPerso", user.getInfosPerso().getDefaultFields())
      .claim("comptes", user.getInfosPerso().getComptes().stream()
        .filter(compte -> !compte.isDeleted())
        .map(compte -> compte.getCustomRoleFields("valeur")).collect(Collectors.toList()))
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpiration()))
      .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
      .compact();

    // @TODO:: Blacklist old accessToken if exist
    String oldToken = user.getCurrentAccessToken();

    user.setAuthenticated(true);
    user.setCurrentAccessToken(newAccessToken);
    userDao.save(user);

    return newAccessToken;
  }

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody().getSubject();
	}

  public Claims getClaimsFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
