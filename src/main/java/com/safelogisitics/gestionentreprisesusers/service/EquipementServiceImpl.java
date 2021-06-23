package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementType;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

public class EquipementServiceImpl implements EquipementService {

  public Collection<Equipement> getEquipements(String fournisseur, EEquipementType type, Pageable pageable) {
    return null;
  }

  public Optional<Equipement> getEquipementById(String id) {
    return null;
  }
  
  public Equipement createEquipement(Equipement equipement) {
    return null;
  }

  public Equipement updateEquipement(String id, Equipement equipement) {
    return null;
  }

  public void deleteEquipement(String id) {
    return;
  }
}
