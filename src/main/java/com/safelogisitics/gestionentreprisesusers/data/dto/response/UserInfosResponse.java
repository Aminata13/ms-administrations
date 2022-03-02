package com.safelogisitics.gestionentreprisesusers.data.dto.response;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.model.User;

public class UserInfosResponse {
  
  private String id;

  private String prenom;

  private String nom;

  private String nomComplet;

  private String photoProfil;

  private String email;

  private String telephone;

  private String adresse;

  private LocalDate dateNaissance;

  private String anneeNaissance;

  private String numeroPiece;

  private String username;

  private Set<Compte> comptes = new HashSet<>();

  private Abonnement abonnement;

  public UserInfosResponse(InfosPersoModel infosPerso, Abonnement abonnement, User user) {
    this.id = infosPerso.getId();
    this.prenom = infosPerso.getPrenom();
    this.nom = infosPerso.getNom();
    this.nomComplet = infosPerso.getNomComplet();
    this.email = infosPerso.getEmail();
    this.telephone = infosPerso.getTelephone();
    this.adresse = infosPerso.getAdresse();
    this.dateNaissance = infosPerso.getDateNaissance();
    this.anneeNaissance = infosPerso.getAnneeNaissance();
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

  public String getNomComplet() {
    return this.nomComplet;
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

  public String getAnneeNaissance() {
    return this.anneeNaissance;
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
    _customRoleField.put("pointGratuites", this.abonnement.getPointGratuites());
    _customRoleField.put("prixCarte", this.abonnement.getPrixCarte());
    _customRoleField.put("depotInitial", this.abonnement.getDepotInitial());
    _customRoleField.put("statut", this.abonnement.getStatut());
    _customRoleField.put("dateCreation", this.abonnement.getDateCreation());

    return _customRoleField;
  }
}
