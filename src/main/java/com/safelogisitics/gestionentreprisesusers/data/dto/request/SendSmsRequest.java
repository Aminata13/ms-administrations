package com.safelogisitics.gestionentreprisesusers.data.dto.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SendSmsRequest {

  private String signature;

  private String subject;

  private String content;

  private Collection<Map<String, String>> recipients = new ArrayList<>();

  public SendSmsRequest() {}

  public SendSmsRequest(String signature, String subject, String content, Collection<String> recipients) {
    this.signature = signature;
    this.subject = subject;
    this.content = content;
    int index = 1;
    for (Iterator<String> value = recipients.iterator(); value.hasNext(); index++) {
      Map<String, String> recipient = new HashMap<>();
      String telephone = value.next();
      if (telephone.startsWith("221"))
        telephone = "221"+telephone;

      recipient.put("id", String.valueOf(index));
      recipient.put("value", telephone);
      this.recipients.add(recipient);
    }
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

  public Collection<Map<String, String>> getRecipients() {
		return this.recipients;
	}

  public void setRecipients(Collection<Map<String,String>> recipients) {
		this.recipients = recipients;
	}

}
