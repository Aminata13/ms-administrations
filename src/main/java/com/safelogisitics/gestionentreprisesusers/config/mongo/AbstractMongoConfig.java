package com.safelogisitics.gestionentreprisesusers.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractMongoConfig extends AbstractMongoClientConfiguration {

  private String host;
  private int port;
  private String username;
  private String password;
  private String database;

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return this.port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDatabase() {
    return this.database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  @Bean
  public MongoClient mongo() throws Exception {
    final String connectionString = username != null && !username.isEmpty() && password != null && !password.isEmpty()
    ? String.format("mongodb://%s:%s@%s:%s/%s", username, password, host, port, database)
    : String.format("mongodb://%s:%s/%s", host, port, database);

    final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(connectionString))
      .build();

    return MongoClients.create(mongoClientSettings);
  }

  abstract public MongoTemplate getMongoTemplate() throws Exception;
}