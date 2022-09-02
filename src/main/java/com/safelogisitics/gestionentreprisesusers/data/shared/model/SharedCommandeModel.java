package com.safelogisitics.gestionentreprisesusers.data.shared.model;

import com.safelogisitics.gestionentreprisesusers.data.enums.EClientType;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.model.AuditMetadata;
import com.safelogisitics.gestionentreprisesusers.data.model.PointGeographique;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.subclass.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "commandes")
@TypeAlias("SharedCommandeModel")
public class SharedCommandeModel extends AuditMetadata {

  @Id
  private String id;

  @Indexed
  @Field(name = "numero")
  private String numero;

  @Indexed
  @Field(name = "type")
  private EServiceType type;

  @Field(name = "libelle")
  private String libelle;

  @Field(name = "demenagement")
  private Demenagement demenagement;

  @Field(name = "pressing")
  private Pressing pressing;

  @Field(name = "bricolage")
  private Bricolage bricolage;

  @Field(name = "banque")
  private Banque banque;

  @Field(name = "course")
  private Course course;

  @Field(name = "visiteTechnique")
  private VisiteTechnique visiteTechnique;

  @Indexed
  @Field(name = "clientType")
  private EClientType clientType;

  @Indexed
  @Field(name = "clientId")
  private String clientId;

  @Indexed
  @Field(name = "prestataireId")
  private String prestataireId;

  @Indexed
  @Field(value = "agentId")
  private String agentId;

  @Indexed
  @Field(name = "typeService")
  private String typeService;

  @Indexed
  @Field(value = "typeLivraison")
  private TypeLivraison typeLivraison;

  @Indexed
  @Field(value = "typeColis")
  private String typeColis;

  @Field(value = "pointDepart")
  private PointGeographique pointDepart;

  @Field(value = "pointArriver")
  private PointGeographique pointArriver;

  @Field(name = "prix")
  private BigDecimal prix = BigDecimal.valueOf(0);

  @Field(name = "prixPrestataire")
  private BigDecimal prixPrestataire = BigDecimal.valueOf(0);

  @Field(name = "valeurEstimer")
  private BigDecimal valeurEstimer = BigDecimal.valueOf(0);

  @Field(value = "gratuite")
  private boolean gratuite = false;

  @Indexed
  @Field(name = "paiementEffectuer")
  private boolean paiementEffectuer = false;

  @Indexed
  @Field(name = "evaluationEffectuer")
  private boolean evaluationEffectuer = false;

  @Indexed
  @Field(name = "affectationEffectuer")
  private boolean affectationEffectuer = false;

  @Field(name = "dateExecutionPrevue")
  private LocalDate dateExecutionPrevue;

  @Field(name = "heureExecutionPrevue")
  private String heureExecutionPrevue;

  @Field(name = "instructionsClient")
  private String instructionsClient;

  @Field(name = "statut")
  private int statut = 0;

  @Field(value = "ordreCommande")
  private int ordreCommande;

  public SharedCommandeModel() {}

  public SharedCommandeModel(String numero) {
    this.numero = numero;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public EServiceType getType() {
    return this.type;
  }

  public void setType(EServiceType type) {
    this.type = type;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public Demenagement getDemenagement() {
    return demenagement;
  }

  public void setDemenagement(Demenagement demenagement) {
    this.demenagement = demenagement;
  }

  public Pressing getPressing() {
    return pressing;
  }

  public void setPressing(Pressing pressing) {
    this.pressing = pressing;
  }

  public Bricolage getBricolage() {
    return bricolage;
  }

  public void setBricolage(Bricolage bricolage) {
    this.bricolage = bricolage;
  }

  public Banque getBanque() {
    return banque;
  }

  public void setBanque(Banque banque) {
    this.banque = banque;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public VisiteTechnique getVisiteTechnique() {
    return visiteTechnique;
  }

  public void setVisiteTechnique(VisiteTechnique visiteTechnique) {
    this.visiteTechnique = visiteTechnique;
  }

  public EClientType getClientType() {
    return this.clientType;
  }

  public void setClientType(EClientType clientType) {
    this.clientType = clientType;
  }

  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getPrestataireId() {
    return this.prestataireId;
  }

  public void setPrestataireId(String prestataireId) {
    this.prestataireId = prestataireId;
  }

  public String getAgentId() {
    return this.agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public String getTypeService() {
    return this.typeService;
  }

  public void setTypeService(String typeService) {
    this.typeService = typeService;
  }

  public TypeLivraison getTypeLivraison() {
    return typeLivraison;
  }

  public void setTypeLivraison(TypeLivraison typeLivraison) {
    this.typeLivraison = typeLivraison;
  }

  public String getTypeColis() {
    return this.typeColis;
  }

  public void setTypeColis(String typeColis) {
    this.typeColis = typeColis;
  }

  public PointGeographique getPointDepart() {
    return pointDepart;
  }

  public void setPointDepart(PointGeographique pointDepart) {
    this.pointDepart = pointDepart;
  }

  public PointGeographique getPointArriver() {
    return pointArriver;
  }

  public void setPointArriver(PointGeographique pointArriver) {
    this.pointArriver = pointArriver;
  }

  public BigDecimal getPrix() {
    return this.prix;
  }

  public void setPrix(BigDecimal prix) {
    this.prix = prix;
  }

  public BigDecimal getPrixPrestataire() {
    return this.prixPrestataire;
  }

  public void setPrixPrestataire(BigDecimal prixPrestataire) {
    this.prixPrestataire = prixPrestataire;
  }

  public BigDecimal getValeurEstimer() {
    return this.valeurEstimer;
  }

  public void setValeurEstimer(BigDecimal valeurEstimer) {
    this.valeurEstimer = valeurEstimer;
  }

  public boolean isGratuite() {
    return this.gratuite;
  }

  public boolean getGratuite() {
    return this.gratuite;
  }

  public void setGratuite(boolean gratuite) {
    this.gratuite = gratuite;
  }

  public boolean isPaiementEffectuer() {
    return this.paiementEffectuer;
  }

  public boolean getPaiementEffectuer() {
    return this.paiementEffectuer;
  }

  public void setPaiementEffectuer(boolean paiementEffectuer) {
    this.paiementEffectuer = paiementEffectuer;
  }

  public boolean isEvaluationEffectuer() {
    return this.evaluationEffectuer;
  }

  public boolean getEvaluationEffectuer() {
    return this.evaluationEffectuer;
  }

  public void setEvaluationEffectuer(boolean evaluationEffectuer) {
    this.evaluationEffectuer = evaluationEffectuer;
  }

  public boolean isAffectationEffectuer() {
    return this.affectationEffectuer;
  }

  public boolean getAffectationEffectuer() {
    return this.affectationEffectuer;
  }

  public void setAffectationEffectuer(boolean affectationEffectuer) {
    this.affectationEffectuer = affectationEffectuer;
  }

  public LocalDate getDateExecutionPrevue() {
    return this.dateExecutionPrevue;
  }

  public void setDateExecutionPrevue(LocalDate dateExecutionPrevue) {
    this.dateExecutionPrevue = dateExecutionPrevue;
  }

  public String getHeureExecutionPrevue() {
    return this.heureExecutionPrevue;
  }

  public void setHeureExecutionPrevue(String heureExecutionPrevue) {
    this.heureExecutionPrevue = heureExecutionPrevue;
  }

  public String getInstructionsClient() {
    return this.instructionsClient;
  }

  public void setInstructionsClient(String instructionsClient) {
    this.instructionsClient = instructionsClient;
  }

  public int getStatut() {
    return this.statut;
  }

  public void setStatut(int statut) {
    this.statut = statut;
  }

  public int getOrdreCommande() {
    return this.ordreCommande;
  }

  public void setOrdreCommande(int ordreCommande) {
    this.ordreCommande = ordreCommande;
  }
}
