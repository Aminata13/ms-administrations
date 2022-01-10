package com.safelogisitics.gestionentreprisesusers.dao;

import com.safelogisitics.gestionentreprisesusers.model.ConsommationCarburant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsommationCarburantDao extends PagingAndSortingRepository<ConsommationCarburant, String> {
}
