package com.safelogisitics.gestionentreprisesusers.dao.filter;

import java.time.LocalDateTime;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.EEquipementType;

public interface EquipementDefaultFIelds {
  
  public String getId();

  public EEquipementType getType();

  public String getLibelle();

  public String getDescription();

  public Set<String> getSpecificites();

  public double getStock();

  public LocalDateTime getDateCreation();

}
