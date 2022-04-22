package com.safelogisitics.gestionentreprisesusers.data.mongoListener;

import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.service.SharedInfosPersoService;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AfterSaveCompteListener extends AbstractMongoEventListener<Compte> {

  private SharedInfosPersoService sharedInfosPersoService;

  public AfterSaveCompteListener(SharedInfosPersoService sharedInfosPersoService) {
    this.sharedInfosPersoService = sharedInfosPersoService;
  }

  @Override
  public void onAfterSave(AfterSaveEvent<Compte> event) {

    Compte compte = event.getSource();

    if (compte.getInfosPersoId() != null) {
      this.sharedInfosPersoService.convertToSharedInfosPersoRequestAndSave(
        compte.getInfosPersoId()
      );
    }
  }
}
