package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Collection;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSCible;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSData;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSRepetition;

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

  @Field(name = "cibles")
  private Set<ESMSCible> cibles;

  @Field(name = "motCle")
  private String motCle;

  @Field(name = "repetition")
  private ESMSRepetition repetition;

  public SMSModel() {}

  public SMSModel(String signature, String subject, String content, Collection<ESMSData> data, Set<ESMSCible> cibles, ESMSRepetition repetition) {
    this.signature = signature;
    this.subject = subject;
    this.content = content;
    this.data = data;
    this.cibles = cibles;
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

  public Set<ESMSCible> getCibles() {
    return this.cibles;
  }

  public void setCibles(Set<ESMSCible> cibles) {
    this.cibles = cibles;
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
}
