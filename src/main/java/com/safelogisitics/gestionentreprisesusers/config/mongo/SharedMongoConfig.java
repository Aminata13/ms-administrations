package com.safelogisitics.gestionentreprisesusers.config.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(
  basePackages = {
    "com.safelogisitics.gestionentreprisesusers.data.shared.dao",
    "com.safelogisitics.gestionentreprisesusers.data.shared.repository",
    "com.safelogisitics.gestionentreprisesusers.data.shared.repository.custom.impl"
  },
  mongoTemplateRef = "sharedMongoTemplate"
)
@ConfigurationProperties(prefix = "mongodb.shared")
public class SharedMongoConfig extends AbstractMongoConfig {

  @Override
  protected String getDatabaseName() {
    return this.getDatabase();
  }

  @Bean(name = "sharedMongoTemplate")
  @Override
  public MongoTemplate getMongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), this.getDatabaseName());
  }
}
