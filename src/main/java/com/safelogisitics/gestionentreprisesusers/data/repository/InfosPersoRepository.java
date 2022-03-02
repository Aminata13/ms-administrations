package com.safelogisitics.gestionentreprisesusers.data.repository;

import com.safelogisitics.gestionentreprisesusers.data.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.repository.custom.InfosPersoRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository
public interface InfosPersoRepository extends InfosPersoDao, InfosPersoRepositoryCustom {

}
