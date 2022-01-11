package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.Map;
import java.util.Set;

public class SMSRequest {

  private String motCle;

  private Map<String, String> data;

  private Set<String> recipients;

  public SMSRequest() {}

  public SMSRequest(String motCle, Map<String, String> data, Set<String> recipients) {
    this.motCle = motCle;
    this.data = data;
    this.recipients = recipients;
  }

  public String getMotCle() {
    return this.motCle;
  }

  public void setMotCle(String motCle) {
    this.motCle = motCle;
  }

  public Map<String, String> getData() {
    return this.data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }

  public Set<String> getRecipients() {
    return this.recipients;
  }

  public void setRecipients(Set<String> recipients) {
    this.recipients = recipients;
  }

}
