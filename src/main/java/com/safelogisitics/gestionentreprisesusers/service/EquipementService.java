package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.FournitureEquipement;

public interface EquipementService {

  public Collection<Equipement> getEquipements(Map<String, String> parameters);

  public Optional<Equipement> getEquipementById(String id);
  
  public Equipement createEquipement(Equipement equipement);

  public Equipement updateEquipement(String id, Equipement equipement);

  public Equipement fournitureEquipement(String id, FournitureEquipement fourniture);

  public void deleteEquipement(String id);
}
