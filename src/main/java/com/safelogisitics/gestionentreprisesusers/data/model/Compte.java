package com.safelogisitics.gestionentreprisesusers.data.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/*
Lister => READ, Créer => CREATE, Editer => UPDATE, Valider => VALIDATE, Affecter => ASSIGN, Tracker => TRACK
[
  gestions des Personnels: [READ, CREATE, UPDATE, ACTIVATE],
  gestions des Agents: [READ, CREATE, UPDATE, ACTIVATE],
  gestions des Clients: [READ, CREATE, UPDATE, ACTIVATE],
  gestions des Commandes de livraisons: [READ, CREATE, UPDATE, VALIDATE, ASSIGN, TRACK],
  gestions des Abonnements: [READ, CREATE, UPDATE, VALIDATE],
  gestions des Rechargements: [READ, CREATE, UPDATE, VALIDATE],
  gestions des Tarifs: [READ, CREATE, UPDATE, VALIDATE]
]
*/

@Document(collection = "comptes")
public class Compte extends AuditMetadata {

  @Id
  private String id;

  @Field(value = "type")
  private ECompteType type;

  @Field(value = "infosPersoId")
  private String infosPersoId;

  @DBRef
  @Field(value = "entreprise")
  private Entreprise entreprise;

  @Field(value = "entrepriseId")
  private String entrepriseId;

  @Field(value = "entrepriseUser")
  private boolean entrepriseUser;

  @DBRef
  @Field(value = "role")
  private Role role;

  @Field(value = "serviceConciergeries")
  private Set<EServiceConciergeType> serviceConciergeries;

  @Field(value = "services")
  private Set<EServiceType> services = new HashSet<>(Arrays.asList(EServiceType.LIVRAISON));

  @Field(value = "equipements")
  private Set<AffectationEquipement> equipements = new HashSet<>();

  @Field(value = "moyenTransportId")
  private String moyenTransportId;

  @Field(value = "numeroEmei")
  private String numeroEmei;

  @Field(value = "numeroReference")
  private String numeroReference;

  @Field(value = "statut")
  private int statut;

  @JsonIgnore
  @Field(value = "deleted")
  private boolean deleted;

  private InfosPerso userInfos;

  private Abonnement abonnement;

  @Field(value = "dateCreation")
  private Date dateCreation;

  public Compte() {
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
  }

  public Compte(ECompteType type, String infosPersoId) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
    if (type.equals(ECompteType.COMPTE_ENTREPRISE)) {
      this.services.add(EServiceType.PRESTATION);
    }
  }

  public Compte(ECompteType type, String infosPersoId, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.statut = statut;
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
    if (type.equals(ECompteType.COMPTE_ENTREPRISE)) {
      this.services.add(EServiceType.PRESTATION);
    }
  }

  public Compte(ECompteType type, String infosPersoId, Role role, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.role = role;
    this.statut = statut;
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
    if (type.equals(ECompteType.COMPTE_ENTREPRISE)) {
      this.services.add(EServiceType.PRESTATION);
    }
  }

  public Compte(ECompteType type, String infosPersoId, Role role, Set<EServiceType> services, int statut) {
    this.type = type;
    this.infosPersoId = infosPersoId;
    this.role = role;
    this.services = services;
    this.statut = statut;
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
    if (type.equals(ECompteType.COMPTE_ENTREPRISE)) {
      this.services.add(EServiceType.PRESTATION);
    }
  }

  public Compte(ECompteType type, Entreprise entreprise, String infosPersoId, Role role, Set<EServiceType> services, int statut) {
    this.type = type;
    this.entreprise = entreprise;
    this.entrepriseId = entreprise.getId();
    this.infosPersoId = infosPersoId;
    this.role = role;
    this.services = services;
    this.statut = statut;
    this.deleted = false;
    this.entrepriseUser = false;
    this.dateCreation = new Date();
    if (type.equals(ECompteType.COMPTE_ENTREPRISE)) {
      this.services.add(EServiceType.PRESTATION);
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ECompteType getType() {
    return this.type;
  }

  public void setType(ECompteType type) {
    this.type = type;
  }

  public String getInfosPersoId() {
    return this.infosPersoId;
  }

  public void setInfosPersoId(String infosPersoId) {
    this.infosPersoId = infosPersoId;
  }

  public Entreprise getEntreprise() {
    return this.entreprise;
  }

  public void setEntreprise(Entreprise entreprise) {
    this.entreprise = entreprise;
    this.entrepriseId = entreprise.getId();
  }

  public String getEntrepriseId() {
    return this.entrepriseId;
  }

  public void setEntrepriseId(String entrepriseId) {
    this.entrepriseId = entrepriseId;
  }

  public boolean isEntrepriseUser() {
    return this.entrepriseUser;
  }

  public void setEntrepriseUser(boolean entrepriseUser) {
    this.entrepriseUser = entrepriseUser;
  }


  public Role getRole() {
    return this.role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Set<EServiceConciergeType> getServiceConciergeries() {
    return this.serviceConciergeries;
  }

  public void setServiceConciergeries(Set<EServiceConciergeType> serviceConciergeries) {
    this.serviceConciergeries = serviceConciergeries;
  }

  public Set<EServiceType> getServices() {
    return this.services;
  }

  public void setServices(Set<EServiceType> services) {
    this.services = services;
  }

  public Set<AffectationEquipement> getEquipements() {
    return this.equipements;
  }

  public void addEquipement(AffectationEquipement equipement) {
    if (this.equipements.contains(equipement)) {
      this.equipements.remove(equipement);
    }
    this.equipements.add(equipement);
  }

  public void removeEquipement(AffectationEquipement equipement) {
    this.equipements.remove(equipement);
  }

  public String getMoyenTransportId() {
    return this.moyenTransportId;
  }

  public void setMoyenTransportId(String moyenTransportId) {
    this.moyenTransportId = moyenTransportId;
  }

  public String getNumeroEmei() {
    return this.numeroEmei;
  }

  public void setNumeroEmei(String numeroEmei) {
    this.numeroEmei = numeroEmei;
  }

  public String getNumeroReference() {
    return this.numeroReference;
  }

  public void setNumeroReference(String numeroReference) {
    this.numeroReference = numeroReference;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public InfosPerso getUserInfos() {
    return this.userInfos;
  }

  public void setUserInfos(InfosPerso userInfos) {
    this.userInfos = userInfos;
  }

  public Abonnement getAbonnement() {
    return this.abonnement;
  }

  public void setAbonnement(Abonnement abonnement) {
    this.abonnement = abonnement;
  }

  public Date getDateCreation() {
    return this.dateCreation;
  }

  public void setDateCreation(Date dateCreation) {
    this.dateCreation = dateCreation;
  }

  public Object getCustomFields() {
    Map <String,Object> _customRoleField = new LinkedHashMap<>();

    _customRoleField.put("id", getId());
    _customRoleField.put("type", getType());
    _customRoleField.put("entreprise", entrepriseId != null ? entrepriseId : "");
    _customRoleField.put("isEntrepriseUser", entreprise != null && entrepriseUser == true ? true : false);
    _customRoleField.put("role", "");
    _customRoleField.put("serviceConciergeries", getServiceConciergeries());
    _customRoleField.put("services", getServices());
    _customRoleField.put("privileges", new ArrayList<>());
    if (role != null) {
      _customRoleField.put("role", role.getLibelle());
      List<String> privileges = new ArrayList<>();
      for (Map.Entry<String, Set<String>> entry : role.getPrivilegesActions().entrySet()) {
        privileges.add(String.format("%s_%s", entry.getKey(), String.join("_", entry.getValue())));
      }
      _customRoleField.put("privileges", privileges);
    }

    return _customRoleField;
  }

  public Object getCustomRoleFields() {
    Map <String,Object> _customRoleField = new LinkedHashMap<>();

    if (getRole() == null) {
      return _customRoleField;
    }

    _customRoleField.put("id", getRole().getId());
    _customRoleField.put("type", getType());
    _customRoleField.put("entreprise", entreprise != null ? entreprise.getId() : "");
    _customRoleField.put("role", getRole().getLibelle());
    _customRoleField.put("privileges", new ArrayList<>());
    List<String> privileges = new ArrayList<>();
    for (Map.Entry<String, Set<String>> entry : role.getPrivilegesActions().entrySet()) {
      privileges.add(String.format("%s_%s", entry.getKey(), String.join("_", entry.getValue())));
    }
    _customRoleField.put("privileges", privileges);

    return _customRoleField;
  }

  public int hashCode() {
    return Objects.hash(type, infosPersoId, entrepriseId, 1000);
  }

  public boolean equals(Object obj) {
    if (obj instanceof Compte) {
        Compte pp = (Compte) obj;
        return (pp.hashCode() == this.hashCode());
    } else {
        return false;
    }
  }
}
