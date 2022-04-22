package com.safelogisitics.gestionentreprisesusers.data.shared.repository;


import com.safelogisitics.gestionentreprisesusers.data.shared.dao.SharedInfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.data.shared.repository.custom.SharedInfosPersoRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository
public interface SharedInfosPersoRepository extends SharedInfosPersoDao, SharedInfosPersoRepositoryCustom {

}
