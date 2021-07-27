package com.safelogisitics.gestionentreprisesusers.config.fcm;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FCMInitializer {

  @Value("${app.firebase-configuration-file}")
  private String firebaseConfigPath;

  Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

  @PostConstruct
  public void initialize() {
    try {
      GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());

      FirebaseOptions firebaseOptions = FirebaseOptions
        .builder()
        .setCredentials(googleCredentials)
        .build();

      if(FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(firebaseOptions);
        logger.info("Firebase application has been initialized");
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}