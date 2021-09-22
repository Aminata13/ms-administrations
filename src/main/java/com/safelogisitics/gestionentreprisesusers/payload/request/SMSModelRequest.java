package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSCible;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSData;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSRepetition;

public class SMSModelRequest {

  @NotBlank(message = "La signature est obligatoire.")
  private String signature;

  @NotBlank(message = "Le sujet est obligatoire.")
  private String subject;

  @NotBlank(message = "Le contenu du message ne doit pas être vide.")
  private String content;

  private Collection<ESMSData> data;

  @NotNull(message = "La cible est obligatoire.")
  private ESMSCible cible;

  @NotNull(message = "Le type répétition est obligatoire.")
  private ESMSRepetition repetition;

  public String getSignature() {
    return this.signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Collection<ESMSData> getData() {
    return this.data;
  }

  public void setData(Collection<ESMSData> data) {
    this.data = data;
  }

  public ESMSCible getCible() {
    return this.cible;
  }

  public void setCible(ESMSCible cible) {
    this.cible = cible;
  }

  public ESMSRepetition getRepetition() {
    return this.repetition;
  }

  public void setRepetition(ESMSRepetition repetition) {
    this.repetition = repetition;
  }

}
