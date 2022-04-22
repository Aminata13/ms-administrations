package com.safelogisitics.gestionentreprisesusers.data.mongoListener;

import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.service.SharedInfosPersoService;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AfterSaveInfosPersoListener extends AbstractMongoEventListener<InfosPersoModel> {

  private SharedInfosPersoService sharedInfosPersoService;

  public AfterSaveInfosPersoListener(SharedInfosPersoService sharedInfosPersoService) {
    this.sharedInfosPersoService = sharedInfosPersoService;
  }

  @Override
  public void onAfterSave(AfterSaveEvent<InfosPersoModel> event) {
    InfosPersoModel infosPerso = event.getSource();

    this.sharedInfosPersoService.convertToSharedInfosPersoRequestAndSave(infosPerso.getId());
  }
}
