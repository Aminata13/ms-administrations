package com.safelogisitics.gestionentreprisesusers.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Field(value = "oid")
  private String oid;
  
  @NotBlank
  @Field(value = "username")
  private String username;

  @NotBlank
  @JsonIgnore
  @Field(value = "password")
  private String password;

  @NotBlank
  @Field(value = "createDate")
  private Date createDate;

  @JsonIgnore
  @Field(value = "status")
  private int status;


  public User() {
  }

  public User(String oid, String username, String password, int status) {
    this.oid = oid;
    this.username = username;
    this.password = password;
    this.createDate = new Date();
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
