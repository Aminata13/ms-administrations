package com.safelogisitics.gestionentreprisesusers.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "smsModels")
public class SMSModel extends AuditMetadata {

  private String id;

  @Field(name = "signature")
  private String signature;

  private String subject;

  private String content;

  private boolean editable;

  private String motCle;

  private LocalDateTime dateLancement;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public boolean isEditable() {
    return this.editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public String getMotCle() {
    return this.motCle;
  }

  public void setMotCle(String motCle) {
    this.motCle = motCle;
  }

  public LocalDateTime getDateLancement() {
    return this.dateLancement;
  }

  public void setDateLancement(LocalDateTime dateLancement) {
    this.dateLancement = dateLancement;
  }

}
