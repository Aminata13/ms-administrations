package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementType;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

public interface EquipementService {

  public Collection<Equipement> getEquipements(String fournisseur, EEquipementType type, Pageable pageable);

  public Optional<Equipement> getEquipementById(String id);
  
  public Equipement createEquipement(Equipement equipement);

  public Equipement updateEquipement(String id, Equipement equipement);

  public void deleteEquipement(String id);
}
