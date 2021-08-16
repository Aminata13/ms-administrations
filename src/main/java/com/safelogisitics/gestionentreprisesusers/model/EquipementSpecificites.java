package com.safelogisitics.gestionentreprisesusers.model;

import java.util.HashMap;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.model.enums.ESpecificiteType;

public class EquipementSpecificites {

  private String libelle;

  private ESpecificiteType type;

  private Map<String, String> data = new HashMap<>();

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public ESpecificiteType getType() {
    return this.type;
  }

  public void setType(ESpecificiteType type) {
    this.type = type;
  }

  public Map<String, String> getData() {
    return this.data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }
}
