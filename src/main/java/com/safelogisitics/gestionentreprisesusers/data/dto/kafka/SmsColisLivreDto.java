package com.safelogisitics.gestionentreprisesusers.data.dto.kafka;

public class SmsColisLivreDto {

    private String particulierId;

    private String entrepriseId;

    public SmsColisLivreDto() {
    }

    public SmsColisLivreDto(String particulierId, String entrepriseId) {
        this.particulierId = particulierId;
        this.entrepriseId = entrepriseId;
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
}
