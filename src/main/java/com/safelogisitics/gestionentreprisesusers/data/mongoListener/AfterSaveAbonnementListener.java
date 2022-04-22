package com.safelogisitics.gestionentreprisesusers.data.mongoListener;

import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.service.SharedInfosPersoService;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AfterSaveAbonnementListener extends AbstractMongoEventListener<Abonnement> {

  private SharedInfosPersoService sharedInfosPersoService;

  public AfterSaveAbonnementListener(SharedInfosPersoService sharedInfosPersoService) {
    this.sharedInfosPersoService = sharedInfosPersoService;
  }

  @Override
  public void onAfterSave(AfterSaveEvent<Abonnement> event) {

    Abonnement abonnement = event.getSource();

    if (abonnement.getCompteClient() != null) {
      this.sharedInfosPersoService.convertToSharedInfosPersoRequestAndSave(
        abonnement.getCompteClient().getInfosPersoId()
      );
    }
  }
}
