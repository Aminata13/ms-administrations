package com.safelogisitics.gestionentreprisesusers.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SMSMessageRequest {
  
  @NotBlank(message = "La signature est obligatoire.")
  private String signature;

  @NotBlank(message = "Le sujet est obligatoire.")
  private String subject;

  @NotBlank(message = "Le contenu du message ne doit pas être vide.")
  private String content;

  @NotNull(message = "La liste des destinataires est obligatoires.")
  private Set<String> recipients; // [221775919686]

  public SMSMessageRequest() {}

  public SMSMessageRequest(String signature, String subject, String content, Set<String> recipients) {
    this.signature = signature;
    this.subject = subject;
    this.content = content;
    this.recipients = recipients;
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

  public Set<String> getRecipients() {
		return this.recipients;
	}

  public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}

}
