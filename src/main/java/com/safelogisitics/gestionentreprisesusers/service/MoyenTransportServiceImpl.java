package com.safelogisitics.gestionentreprisesusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.safelogisitics.gestionentreprisesusers.dao.MoyenTransportDao;
import com.safelogisitics.gestionentreprisesusers.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.model.enums.EMoyenTransportType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

public class MoyenTransportServiceImpl implements MoyenTransportService {

  @Autowired
  MoyenTransportDao moyenTransportDao;

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public Page<MoyenTransport> getMoyenTransports(String fournisseur, EMoyenTransportType type, String reference, String marque, String modele, Pageable pageable) {
    final Query query = new Query().with(pageable);

    final List<Criteria> listCriteria = new ArrayList<>();

    if (fournisseur != null && !fournisseur.isEmpty())
      listCriteria.add(Criteria.where("fournisseur").is(fournisseur));

    if (type != null)
      listCriteria.add(Criteria.where("type").is(type));

    if (reference != null && !reference.isEmpty())
      listCriteria.add(Criteria.where("reference").is(reference));

    if (marque != null && !marque.isEmpty())
      listCriteria.add(Criteria.where("marque").is(marque));

    if (modele != null && !modele.isEmpty())
      listCriteria.add(Criteria.where("modele").is(modele));

    if (!listCriteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));

    query.addCriteria(new Criteria().orOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));

    List<MoyenTransport> moyenTransports = mongoTemplate.find(query, MoyenTransport.class);

    return PageableExecutionUtils.getPage(
      moyenTransports, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MoyenTransport.class));
  }

  @Override
  public Optional<MoyenTransport> getMoyenTransportById(String id) {
    return moyenTransportDao.findById(id);
  }

  @Override
  public MoyenTransport createMoyenTransport(MoyenTransport moyenTransport) {
    return null;
  }

  @Override
  public MoyenTransport updateMoyenTransport(String id, MoyenTransport moyenTransport) {
    return null;
  }

  @Override
  public void deleteMoyenTransport(String id) {
    return;
  }
}
