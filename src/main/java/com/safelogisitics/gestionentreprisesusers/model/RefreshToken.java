package com.safelogisitics.gestionentreprisesusers.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "refreshTokens")
public class RefreshToken {
  @Id
  private String id;

  @DBRef
  @Field(value = "user")
  private User user;

  @Field(value = "token")
  private String token;

  @Field(value = "expiryDate")
  private Instant expiryDate;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Instant getExpiryDate() {
    return this.expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }
}
