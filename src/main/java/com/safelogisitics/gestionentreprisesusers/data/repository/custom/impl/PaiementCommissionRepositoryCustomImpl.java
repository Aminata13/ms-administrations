package com.safelogisitics.gestionentreprisesusers.data.repository.custom.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.CommissionSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.PaiementCommissionModel;
import com.safelogisitics.gestionentreprisesusers.data.repository.custom.PaiementCommissionRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PaiementCommissionRepositoryCustomImpl implements PaiementCommissionRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<PaiementCommissionModel> customSearch(CommissionSearchRequestDto commissionSearch, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "createdDate"));
    final List<Criteria> criterias = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    if (commissionSearch != null && commissionSearch.getId() != null && !commissionSearch.getId().isEmpty())
      criterias.add(Criteria.where("id").is(commissionSearch.getId()));

    if (commissionSearch != null && commissionSearch.getIds() != null && !commissionSearch.getIds().isEmpty())
      criterias.add(Criteria.where("id").in(commissionSearch.getIds()));

    if (commissionSearch != null && commissionSearch.getCommandeIds() != null && !commissionSearch.getCommandeIds().isEmpty())
      criterias.add(Criteria.where("commmandeIds").in(commissionSearch.getCommandeIds()));

    if (commissionSearch != null && commissionSearch.getResponsableId() != null && !commissionSearch.getResponsableId().isEmpty())
      criterias.add(Criteria.where("responsableId").is(commissionSearch.getResponsableId()));

    if (commissionSearch != null && commissionSearch.getAuteurId() != null && !commissionSearch.getAuteurId().isEmpty())
      criterias.add(Criteria.where("auteurId").is(commissionSearch.getAuteurId()));

    if (commissionSearch != null && commissionSearch.getPaiementMethode() != null)
      criterias.add(Criteria.where("paiementMethode").is(commissionSearch.getPaiementMethode()));

    if (commissionSearch != null && commissionSearch.getDateDebut() != null && !commissionSearch.getDateDebut().isEmpty())
      criterias.add(Criteria.where("createdDate").gte(LocalDateTime.parse(commissionSearch.getDateDebut(), formatter)));

    if (commissionSearch != null && commissionSearch.getDateFin() != null && !commissionSearch.getDateFin().isEmpty())
      criterias.add(Criteria.where("createdDate").lte(LocalDateTime.parse(commissionSearch.getDateFin(), formatter)));

    if (commissionSearch != null && (commissionSearch.getAnnee() != null || commissionSearch.getMois() != null)) {
      LocalDateTime dateDebut = LocalDateTime.now().withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
      if (commissionSearch.getAnnee() != null) {
        dateDebut = dateDebut.withYear(commissionSearch.getAnnee().intValue());
      }

      if (commissionSearch.getMois() != null) {
        dateDebut = dateDebut.withMonth(commissionSearch.getMois().intValue());
      }

      LocalDateTime dateFin = commissionSearch.getAnnee() != null && commissionSearch.getMois() == null ? 
        dateDebut.plusYears(1) : dateDebut.plusMonths(1);

      criterias.add(Criteria.where("createdDate").gte(dateDebut));
      criterias.add(Criteria.where("createdDate").lte(dateFin));
    }

    if (!criterias.isEmpty())
      query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

    List<PaiementCommissionModel> listServices = mongoTemplate.find(query, PaiementCommissionModel.class);

    return PageableExecutionUtils.getPage(
      listServices, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), PaiementCommissionModel.class));
  }

}
