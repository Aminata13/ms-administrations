package com.safelogisitics.gestionentreprisesusers.model;

import java.util.HashMap;
import java.util.Map;

public class PushNotification {
  
  private String title;

  private String message;

  private Map<String, String> data = new HashMap<>();

  private String topic;

  private String token;

  public PushNotification() {}

  public PushNotification(String title, String message, String topic) {
    this.title = title;
    this.message = message;
    this.topic = topic;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, String> getData() {
    return this.data;
  }

  public void setData(Map<String, String> data) {
    this.data = data;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTopic() {
    return this.topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
