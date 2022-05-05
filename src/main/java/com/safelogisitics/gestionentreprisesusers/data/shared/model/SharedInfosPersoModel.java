package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceConciergeType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.enums.ETypePartenariat;
import com.safelogisitics.gestionentreprisesusers.data.model.AuditMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "infosPersos")
@TypeAlias("SharedInfosPersoModel")
public class SharedInfosPersoModel extends AuditMetadata {

    @Id
    private String id;

    private String prenom;

    private String nom;

    private String photoProfil;

    @Indexed
    private String email;

    @Indexed
    private String telephone;

    private String adresse;

    private LocalDate dateNaissance;

    private String anneeNaissance;

    @Indexed
    private String numeroPermis;

    @Indexed
    private String numeroPiece;

    private Set<Compte> comptes;

    private Abonnement abonnement;

    private LocalDateTime dateCreation;

    public SharedInfosPersoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public String getNomComplet() {
        return String.format("%s %s", this.prenom, this.nom);
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhotoProfil() {
        return this.photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAnneeNaissance() {
        return this.anneeNaissance;
    }

    public void setAnneeNaissance(String anneeNaissance) {
        this.anneeNaissance = anneeNaissance;
    }

    public String getNumeroPermis() {
        return this.numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public String getNumeroPiece() {
        return this.numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Set<Compte> getComptes() {
        return this.comptes;
    }

    public void setComptes(ObjectMapper objectMapper, Set<com.safelogisitics.gestionentreprisesusers.data.model.Compte> dataComptes) {
        this.comptes = new HashSet<Compte>();
        dataComptes.forEach(compte -> {
            Compte tempCompte = objectMapper.convertValue(compte, Compte.class);
            tempCompte.setEntrepriseId(compte.getEntrepriseId());
            tempCompte.setEntrepriseUser(compte.isEntrepriseUser());
            this.comptes.add(tempCompte);
        });
    }

    public Abonnement getAbonnement() {
        return this.abonnement;
    }

    public void setAbonnement(ObjectMapper objectMapper,
                              com.safelogisitics.gestionentreprisesusers.data.model.Abonnement dataAbonnement) {
        this.abonnement = objectMapper.convertValue(dataAbonnement, Abonnement.class);
    }

    public LocalDateTime getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}

class Compte {

    private String id;

    private ECompteType type;

    private String entrepriseId;

    private boolean entrepriseUser;

    private Role role;

    private Set<EServiceConciergeType> serviceConciergeries;

    private Set<EServiceType> services;

    private Set<AffectationEquipement> equipements;

    private String moyenTransportId;

    private String numeroEmei;

    private String numeroReference;

    private int statut;

    @JsonIgnore
    private boolean deleted;

    private LocalDateTime dateCreation;

    public Compte() {
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

    public String getEntrepriseId() {
        return entrepriseId;
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

    public void setEquipements(Set<AffectationEquipement> equipements) {
        this.equipements = equipements;
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

    public LocalDateTime getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

}

class Role {

    private String id;

    private String libelle;

    @JsonIgnore
    private boolean editable;

    @JsonIgnore
    private String type;

    private Map<String, Set<String>> privilegesActions;

    public Role() {
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

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Set<String>> getPrivilegesActions() {
        return this.privilegesActions;
    }

    public void setPrivilegesActions(Map<String, Set<String>> privilegesActions) {
        this.privilegesActions = privilegesActions;
    }

    public boolean hasPrivilegeAction(String _privilege) {
        for (String privilege : this.privilegesActions.keySet()) {
            if (privilege.equals(_privilege)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPrivilegeAction(String _privilege, String action) {
        for (String privilege : this.privilegesActions.keySet()) {
            if (privilege.equals(_privilege) && this.privilegesActions.get(_privilege).contains(action)) {
                return true;
            }
        }
        return false;
    }
}

class TypeAbonnement {

    private String id;

    private String libelle;

    private String icon;

    private int reduction;

    private BigDecimal prix;

    private Set<String> services;

    private Set<String> compteEligibles;

    private int statut;

    public TypeAbonnement() {
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

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getReduction() {
        return this.reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    public BigDecimal getPrix() {
        return this.prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public Set<String> getServices() {
        return services;
    }

    public void setServices(Set<String> services) {
        this.services = services;
    }

    public Set<String> getCompteEligibles() {
        return compteEligibles;
    }

    public void setCompteEligibles(Set<String> compteEligibles) {
        this.compteEligibles = compteEligibles;
    }

    public int getStatut() {
        return this.statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
}

class Abonnement {

    private String id;

    private TypeAbonnement typeAbonnement;

    private Compte compteClient;

    private SharedEntrepriseModel entreprise;

    private Compte compteCreateur;

    private String responsableId;

    private String numeroCarte;

    private boolean carteBloquer;

    private BigDecimal solde;

    private long pointGratuites;

    private BigDecimal prixCarte;

    private BigDecimal depotInitial;

    private int statut;

    @JsonIgnore
    private boolean deleted;

    private LocalDate dateCreation;

    public Abonnement() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypeAbonnement getTypeAbonnement() {
        return this.typeAbonnement;
    }

    public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public Compte getCompteClient() {
        return this.compteClient;
    }

    public void setCompteClient(Compte compteClient) {
        this.compteClient = compteClient;
    }

    public SharedEntrepriseModel getEntreprise() {
        return this.entreprise;
    }

    public void setEntreprise(SharedEntrepriseModel entreprise) {
        this.entreprise = entreprise;
    }

    public Compte getCompteCreateur() {
        return this.compteCreateur;
    }

    public void setCompteCreateur(Compte compteCreateur) {
        this.compteCreateur = compteCreateur;
    }

    public String getResponsableId() {
        return this.responsableId;
    }

    public void setResponsableId(String responsableId) {
        this.responsableId = responsableId;
    }

    public String getNumeroCarte() {
        return this.numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public boolean isCarteBloquer() {
        return this.carteBloquer;
    }

    public void setCarteBloquer(boolean carteBloquer) {
        this.carteBloquer = carteBloquer;
    }

    public BigDecimal getSolde() {
        return this.solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public void rechargerSolde(BigDecimal montant) {
        this.solde = this.solde.add(montant);
    }

    public void debiterSolde(BigDecimal montant) {
        this.solde = this.solde.subtract(montant);
    }

    public long getPointGratuites() {
        return this.pointGratuites;
    }

    public void rechargerPointGratuites(long points) {
        this.pointGratuites = this.pointGratuites + points;
    }

    public void debiterPointGratuites(long points) {
        this.pointGratuites = this.pointGratuites - points;
    }

    public BigDecimal getPrixCarte() {
        return this.prixCarte;
    }

    public void setPrixCarte(BigDecimal prixCarte) {
        this.prixCarte = prixCarte;
    }

    public BigDecimal getDepotInitial() {
        return this.depotInitial;
    }

    public void setDepotInitial(BigDecimal depotInitial) {
        this.depotInitial = depotInitial;
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

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
}

class AffectationEquipement {

    private String idEquipement;

    private double quantite;

    private Map<String, String> specificites;

    public String getIdEquipement() {
        return this.idEquipement;
    }

    public void setIdEquipement(String idEquipement) {
        this.idEquipement = idEquipement;
    }

    public double getQuantite() {
        return this.quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Map<String, String> getSpecificites() {
        return this.specificites;
    }

    public void setSpecificites(Map<String, String> specificites) {
        this.specificites = specificites;
    }
}