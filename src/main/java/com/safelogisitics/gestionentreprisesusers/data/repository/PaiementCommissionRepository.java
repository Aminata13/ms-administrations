package com.safelogisitics.gestionentreprisesusers.data.repository;

import com.safelogisitics.gestionentreprisesusers.data.dao.PaiementCommissionDao;
import com.safelogisitics.gestionentreprisesusers.data.repository.custom.PaiementCommissionRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository
public interface PaiementCommissionRepository extends PaiementCommissionDao, PaiementCommissionRepositoryCustom {

}
