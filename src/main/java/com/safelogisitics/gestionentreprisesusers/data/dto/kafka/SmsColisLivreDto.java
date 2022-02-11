package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

public class SmsColisLivreDto {

    private String particulierId;

    private String entrepriseId;

    private String numeroCommande;

    public SmsColisLivreDto() {
    }

    public SmsColisLivreDto(String particulierId, String entrepriseId, String numeroCommande) {
        this.particulierId = particulierId;
        this.entrepriseId = entrepriseId;
        this.numeroCommande = numeroCommande;
    }

    public String getParticulierId() {
        return particulierId;
    }

    public void setParticulierId(String particulierId) {
        this.particulierId = particulierId;
    }

    public String getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(String entrepriseId) {
        this.entrepriseId = entrepriseId;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }
}
