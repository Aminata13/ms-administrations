package com.safelogisitics.gestionentreprisesusers.payload.response;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.User;

public class UserInfosResponse {
  
  private String id;

  private String prenom;

  private String nom;

  private String photoProfil;

  private String email;

  private String telephone;

  private String adresse;

  private LocalDate dateNaissance;

  private String numeroPiece;

  private String username;

  private Set<Compte> comptes = new HashSet<>();

  private Abonnement abonnement;

  public UserInfosResponse(InfosPerso infosPerso, Abonnement abonnement, User user) {
    this.id = infosPerso.getId();
    this.prenom = infosPerso.getPrenom();
    this.nom = infosPerso.getNom();
    this.email = infosPerso.getEmail();
    this.telephone = infosPerso.getTelephone();
    this.dateNaissance = infosPerso.getDateNaissance();
    this.numeroPiece = infosPerso.getNumeroPiece();
    this.comptes = infosPerso.getComptes();
    this.photoProfil = infosPerso.getPhotoProfil();
    this.abonnement = abonnement;
    this.username = user.getUsername();
  }

  public String getId() {
    return this.id;
  }

  public String getPrenom() {
    return this.prenom;
  }

  public String getNom() {
    return this.nom;
  }

  public String getEmail() {
    return this.email;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public String getAdresse() {
    return this.adresse;
  }

  public LocalDate getDateNaissance() {
    return this.dateNaissance;
  }

  public String getNumeroPiece() {
    return this.numeroPiece;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPhotoProfil() {
    return this.photoProfil;
  }

  public Set<Compte> getComptes() {
    return this.comptes;
  }

  public Map<String, Object> getAbonnement() {
    Map<String, Object> _customRoleField = new LinkedHashMap<>();

    if (this.abonnement == null)
      return _customRoleField;

    _customRoleField.put("id", this.abonnement.getId());
    _customRoleField.put("typeAbonnement", this.abonnement.getTypeAbonnement());
    _customRoleField.put("numeroCarte", this.abonnement.getNumeroCarte());
    _customRoleField.put("carteBloquer", this.abonnement.isCarteBloquer());
    _customRoleField.put("solde", this.abonnement.getSolde());
    _customRoleField.put("statut", this.abonnement.getStatut());
    _customRoleField.put("dateCreation", this.abonnement.getDateCreation());

    return _customRoleField;
  }
}
