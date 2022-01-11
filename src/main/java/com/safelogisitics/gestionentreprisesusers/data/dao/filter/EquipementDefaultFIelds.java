package com.safelogisitics.gestionentreprisesusers.data.dao.filter;

import java.time.LocalDateTime;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.enums.EEquipementCategorie;

public interface EquipementDefaultFIelds {
  
  public String getId();

  public EEquipementCategorie getCategories();

  public String getLibelle();

  public String getDescription();

  public Set<String> getSpecificites();

  public double getStock();

  public LocalDateTime getDateCreation();

}
