package com.safelogisitics.gestionentreprisesusers.dao;

import com.safelogisitics.gestionentreprisesusers.model.Evenement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementDao extends PagingAndSortingRepository<Evenement, String> {
}