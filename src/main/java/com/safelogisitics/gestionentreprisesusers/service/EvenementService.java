package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.EvenementDto;
import com.safelogisitics.gestionentreprisesusers.data.model.Evenement;

public interface EvenementService {
  
  public Collection<EvenementDto> getEvenements(String dateDebut, String dateFin);

  public EvenementDto getEvenementById(String id);

  public EvenementDto createEvenement(Evenement evenement);

  public EvenementDto updateEvenement(String id, Evenement evenement);

  public void deleteEvenement(String id);
}
