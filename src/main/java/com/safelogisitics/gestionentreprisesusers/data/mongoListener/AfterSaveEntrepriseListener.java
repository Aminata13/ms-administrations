package com.safelogisitics.gestionentreprisesusers.data.mongoListener;

import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.service.SharedEntrepriseService;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class AfterSaveEntrepriseListener extends AbstractMongoEventListener<Entreprise> {

  private SharedEntrepriseService sharedEntrepriseService;

  public AfterSaveEntrepriseListener(SharedEntrepriseService sharedEntrepriseService) {
    this.sharedEntrepriseService = sharedEntrepriseService;
  }

  @Override
  public void onAfterSave(AfterSaveEvent<Entreprise> event) {

    Entreprise entreprise = event.getSource();

    if (entreprise.getId() != null) {
      this.sharedEntrepriseService.createOrUpdateEntreprise(entreprise);
    }
  }
}
