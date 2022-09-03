package com.safelogisitics.gestionentreprisesusers.data.shared.dao;

import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedCommandeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SharedCommandeDao extends MongoRepository<SharedCommandeModel, String> {

  public Optional<SharedCommandeModel> findByNumero(String numero);

  public boolean existsByNumero(String numero);

}
