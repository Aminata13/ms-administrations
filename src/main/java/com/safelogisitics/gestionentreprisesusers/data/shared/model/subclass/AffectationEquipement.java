package com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass;

import java.util.Map;

public class AffectationEquipement {

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
