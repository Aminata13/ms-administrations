package com.safelogisitics.gestionentreprisesusers.data.dao;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.enums.ETypeProspect;
import com.safelogisitics.gestionentreprisesusers.data.model.Prospect;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectDao extends PagingAndSortingRepository<Prospect, String> {
  Page<Prospect> findByStatutProspectionOrderByDateCreationDesc(int statutProspection, Pageable pageable);

  Page<Prospect> findByType(ETypeProspect typeProspect, Pageable pageable);

  Optional<Prospect> findByInfosParticulierEmailOrInfosParticulierTelephone(String email, String telephone);

  Optional<Prospect> findByInfosEntrepriseDenomination(String denomination);

  boolean existsByInfosParticulierEmailOrInfosParticulierTelephone(String email, String telephone);

  boolean existsByInfosParticulierEmail(String email);

  boolean existsByInfosParticulierTelephone(String telephone);

  boolean existsByInfosEntrepriseDenominationOrInfosEntrepriseNinea(String denomination, String ninea);

  boolean existsByInfosEntrepriseDenomination(String denomination);

  boolean existsByInfosEntrepriseNinea(String ninea);
}