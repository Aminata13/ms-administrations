package com.safelogisitics.gestionentreprisesusers.data.repository;

import com.safelogisitics.gestionentreprisesusers.data.dao.CommissionDao;
import com.safelogisitics.gestionentreprisesusers.data.repository.custom.CommissionRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository
public interface CommissionRepository extends CommissionDao, CommissionRepositoryCustom {

}
