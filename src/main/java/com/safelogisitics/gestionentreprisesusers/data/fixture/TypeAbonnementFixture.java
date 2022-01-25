package com.safelogisitics.gestionentreprisesusers.data.fixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.TypeAbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.model.TypeAbonnement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TypeAbonnementFixture implements CommandLineRunner {

  private TypeAbonnementDao typeAbonnementDao;

  private ObjectMapper objectMapper;

  public TypeAbonnementFixture(TypeAbonnementDao typeAbonnementDao, ObjectMapper objectMapper) {
    this.typeAbonnementDao = typeAbonnementDao;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    TypeAbonnement[] newTypeAbonnements = objectMapper.readValue(getClass().getResourceAsStream("/data/typeAbonnements.json"), TypeAbonnement[].class);

    for (TypeAbonnement _typeAbonnement : newTypeAbonnements) {
      typeAbonnementDao.findByLibelle(_typeAbonnement.getLibelle()).ifPresentOrElse((typeAbonnement) -> {
        typeAbonnement.setLibelle(_typeAbonnement.getLibelle());
        typeAbonnement.setIcon(_typeAbonnement.getIcon());
        typeAbonnement.setReduction(_typeAbonnement.getReduction());
        typeAbonnement.setPrix(_typeAbonnement.getPrix());
        typeAbonnement.setStatut(_typeAbonnement.getStatut());
        typeAbonnement.setServices(_typeAbonnement.getServices());
        typeAbonnementDao.save(typeAbonnement);
      }, () -> {
        TypeAbonnement typeAbonnement = new TypeAbonnement(_typeAbonnement.getLibelle(), _typeAbonnement.getReduction(), _typeAbonnement.getStatut());
        typeAbonnement.setPrix(_typeAbonnement.getPrix());
        typeAbonnement.setServices(_typeAbonnement.getServices());
        typeAbonnementDao.save(typeAbonnement);
      });
    }
  }
}
