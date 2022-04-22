package com.safelogisitics.gestionentreprisesusers.config.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(
  basePackages = {
    "com.safelogisitics.gestionentreprisesusers.data.dao",
    "com.safelogisitics.gestionentreprisesusers.data.repository",
    "com.safelogisitics.gestionentreprisesusers.data.repository.custom.impl"
  },
  mongoTemplateRef = "mongoTemplate"
)
@ConfigurationProperties(prefix = "mongodb.primary")
public class PrimaryMongoConfig extends AbstractMongoConfig {

  @Override
  protected String getDatabaseName() {
    return this.getDatabase();
  }

  @Primary
  @Bean(name = "mongoTemplate")
  @Override
  public MongoTemplate getMongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), this.getDatabaseName());
  }
}
