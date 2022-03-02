package com.safelogisitics.gestionentreprisesusers.web.security.jwt;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.safelogisitics.gestionentreprisesusers.data.dao.UserDao;
import com.safelogisitics.gestionentreprisesusers.data.model.User;
import com.safelogisitics.gestionentreprisesusers.web.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final JwtConfig jwtConfig;
    private UserDao userDao;
    private AbonnementDao abonnementDao;
    private KafkaTemplate<String, String> blacklistTemplate;

    @Value(value = "${kafka.topics.blacklistAccesstoken.name}")
    private String blacklistAccesstokenName;

    public JwtUtils(JwtConfig jwtConfig, UserDao userDao, AbonnementDao abonnementDao, KafkaTemplate<String, String> blacklistTemplate) {
        this.jwtConfig = jwtConfig;
        this.userDao = userDao;
        this.abonnementDao = abonnementDao;
        this.blacklistTemplate = blacklistTemplate;
    }

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

        Set<Compte> comptes = user.getInfosPerso().getComptes();
        Set<Abonnement> abonnements = new HashSet<>();

        Optional<Compte> compteParticulier = comptes.stream().filter(c -> c.getType().equals(ECompteType.COMPTE_PARTICULIER)).findFirst();
        if (compteParticulier.isPresent()) {
            Optional<Abonnement> abonnement = abonnementDao.findByCompteClientIdAndDeletedIsFalse(compteParticulier.get().getId());
            if (abonnement.isPresent()) {
                abonnements.add(abonnement.get());
            }
        }

        Optional<Compte> compteEntreprise = comptes.stream().filter(c -> c.getType().equals(ECompteType.COMPTE_ENTREPRISE) && c.isEntrepriseUser()).findFirst();
        if (compteEntreprise.isPresent()) {
            Optional<Abonnement> abonnement = abonnementDao.findByEntrepriseId(compteEntreprise.get().getEntrepriseId());
            if (abonnement.isPresent()) {
                abonnements.add(abonnement.get());
            }
        }

        String newAccessToken = Jwts.builder()
                .setSubject((user.getUsername()))
                .claim("id", user.getInfosPerso().getId())
                .claim("username", user.getUsername())
                .claim("infosPerso", user.getInfosPerso().getDefaultFields())
                .claim("comptes", user.getInfosPerso().getComptes().stream()
                        .filter(compte -> !compte.isDeleted())
                        .map(compte -> compte.getCustomFields())
                        .collect(Collectors.toList()))
                .claim("abonnements", abonnements.stream()
                        .map(abonnement -> abonnement.getCustomFields())
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();

        // Blacklist old accessToken if exist
        blacklistAccesstoken(user.getCurrentAccessToken());

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

    public void blacklistAccesstoken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return;
        }
        blacklistTemplate.send(blacklistAccesstokenName, accessToken);
    }
}
