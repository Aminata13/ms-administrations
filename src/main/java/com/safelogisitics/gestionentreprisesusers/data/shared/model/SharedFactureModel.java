package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import com.safelogisitics.gestionentreprisesusers.data.model.AuditMetadata;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.PaiementFacture;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "factures")
@TypeAlias("SharedFactureModel")
public class SharedFactureModel extends AuditMetadata {

    @Id
    private String id;

    @Field(value = "clientId")
    private String clientId;

    @Field(value = "annee")
    private int annee;

    @Field(value = "mois")
    private int mois;

    @Field(value = "montantTotal")
    private BigDecimal montantTotal  = BigDecimal.ZERO;

    @Field(value = "montantRestant")
    private BigDecimal montantRestant  = BigDecimal.ZERO;

    @Field(value = "totalCommandes")
    private int totalCommandes;

    @Field(value = "paiements")
    private Set<PaiementFacture> paiements = new HashSet<>();

    @Field(value = "paiementEffectuer")
    private boolean paiementEffectuer;

    public SharedFactureModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public BigDecimal getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(BigDecimal montantRestant) {
        this.montantRestant = montantRestant;
    }

    public int getTotalCommandes() {
        return totalCommandes;
    }

    public void setTotalCommandes(int totalCommandes) {
        this.totalCommandes = totalCommandes;
    }

    public Set<PaiementFacture> getPaiements() {
        return paiements;
    }

    public void setPaiements(Set<PaiementFacture> paiements) {
        this.paiements = paiements;
    }

    public boolean isPaiementEffectuer() {
        return paiementEffectuer;
    }

    public void setPaiementEffectuer(boolean paiementEffectuer) {
        this.paiementEffectuer = paiementEffectuer;
    }
}
