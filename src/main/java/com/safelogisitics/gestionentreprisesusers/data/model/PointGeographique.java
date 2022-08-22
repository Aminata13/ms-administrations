package com.safelogisitics.gestionentreprisesusers.data.model;

public class PointGeographique {

  private String id;

  private String libelle;

  private String latitude;

  private String longitude;


  public PointGeographique(String libelle, String latitude, String longitude) {
    this.libelle = libelle;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getLatitude() {
    return this.latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return this.longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (this.id != null) stringBuilder.append(this.id);
    if (this.libelle != null) stringBuilder.append(this.libelle);
    if (this.latitude != null) stringBuilder.append(this.latitude);
    if (this.longitude != null) stringBuilder.append(this.longitude);

    return stringBuilder.toString();
  }
}
