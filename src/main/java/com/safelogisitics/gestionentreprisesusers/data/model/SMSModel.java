package com.safelogisitics.gestionentreprisesusers.data.model;

import java.util.Collection;
import java.util.Map;

import com.safelogisitics.gestionentreprisesusers.data.model.enums.ESMSCible;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ESMSData;
import com.safelogisitics.gestionentreprisesusers.data.model.enums.ESMSRepetition;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "smsModels")
public class SMSModel extends AuditMetadata {

  @Id
  private String id;

  @Field(name = "signature")
  private String signature;

  @Field(name = "subject")
  private String subject;

  @Field(name = "content")
  private String content;

  @Field(name = "data")
  private Collection<ESMSData> data;

  @Field(name = "cible")
  private ESMSCible cible;

  @Field(name = "motCle")
  private String motCle;

  @Field(name = "repetition")
  private ESMSRepetition repetition;

  public SMSModel() {}

  public SMSModel(String signature, String subject, String content, Collection<ESMSData> data, ESMSCible cible, ESMSRepetition repetition) {
    this.signature = signature;
    this.subject = subject;
    this.content = content;
    this.data = data;
    this.cible = cible;
    this.repetition = repetition;
  }

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

  public String getMotCle() {
    return this.motCle;
  }

  public void setMotCle(String motCle) {
    this.motCle = motCle;
  }

  public ESMSRepetition getRepetition() {
    return this.repetition;
  }

  public void setRepetition(ESMSRepetition repetition) {
    this.repetition = repetition;
  }

  public String createSms(Collection<String> data, Collection<Map<String, String>> recipients) {
    return String.format("%s %s %s %s", signature, subject, String.format(content, data), recipients);
  }
}
