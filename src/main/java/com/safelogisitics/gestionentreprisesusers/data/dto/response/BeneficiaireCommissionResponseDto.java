package com.safelogisitics.gestionentreprisesusers.data.dto.response;

public class BeneficiaireCommissionResponseDto {

    private String infosPersoId;

    private String prenom;

    private String nom;

    public BeneficiaireCommissionResponseDto() {
    }

    public BeneficiaireCommissionResponseDto(String infosPersoId, String prenom, String nom) {
        this.infosPersoId = infosPersoId;
        this.prenom = prenom;
        this.nom = nom;
    }

    public String getInfosPersoId() {
        return infosPersoId;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }
}
