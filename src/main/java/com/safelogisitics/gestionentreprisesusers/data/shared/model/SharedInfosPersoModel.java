package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.model.AuditMetadata;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.Compte;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
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