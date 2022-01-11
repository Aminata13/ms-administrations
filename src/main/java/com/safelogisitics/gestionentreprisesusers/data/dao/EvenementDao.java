package com.safelogisitics.gestionentreprisesusers.data.dao;

import com.safelogisitics.gestionentreprisesusers.data.model.Evenement;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvenementDao extends PagingAndSortingRepository<Evenement, String> {
}