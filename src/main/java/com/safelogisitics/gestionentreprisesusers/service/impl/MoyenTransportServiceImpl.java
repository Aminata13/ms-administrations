package com.safelogisitics.gestionentreprisesusers.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.dao.MoyenTransportDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.MoyenTransportSearchDto;
import com.safelogisitics.gestionentreprisesusers.data.model.MoyenTransport;
import com.safelogisitics.gestionentreprisesusers.service.MoyenTransportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
public class MoyenTransportServiceImpl implements MoyenTransportService {

  @Autowired
  MoyenTransportDao moyenTransportDao;

  @Autowired
  @Qualifier(value = "mongoTemplate")
  MongoTemplate mongoTemplate;

  @Override
  public Page<MoyenTransport> getMoyenTransports(MoyenTransportSearchDto moyenTransportSearch, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "dateCreation"));
    final List<Criteria> listCriteria = new ArrayList<>();

    if (moyenTransportSearch != null && moyenTransportSearch.getType() != null)
      listCriteria.add(Criteria.where("type").regex(".*"+moyenTransportSearch.getType().name().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getReference() != null && !moyenTransportSearch.getReference().isEmpty())
      listCriteria.add(Criteria.where("reference").regex(".*"+moyenTransportSearch.getReference().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getMarque() != null && !moyenTransportSearch.getMarque().isEmpty())
      listCriteria.add(Criteria.where("marque").regex(".*"+moyenTransportSearch.getMarque().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getModele() != null && !moyenTransportSearch.getModele().isEmpty())
      listCriteria.add(Criteria.where("modele").regex(".*"+moyenTransportSearch.getModele().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getNumeroSerie() != null && !moyenTransportSearch.getNumeroSerie().isEmpty())
      listCriteria.add(Criteria.where("numeroSerie").regex(".*"+moyenTransportSearch.getNumeroSerie().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getNumeroCarteGrise() != null && !moyenTransportSearch.getNumeroCarteGrise().isEmpty())
      listCriteria.add(Criteria.where("numeroCarteGrise").regex(".*"+moyenTransportSearch.getNumeroCarteGrise().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getFournisseur() != null && !moyenTransportSearch.getFournisseur().isEmpty())
      listCriteria.add(Criteria.where("fournisseur").regex(".*"+moyenTransportSearch.getFournisseur().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getMatricule() != null && !moyenTransportSearch.getMatricule().isEmpty())
      listCriteria.add(Criteria.where("matricule").regex(".*"+moyenTransportSearch.getMatricule().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getNumeroCommande() != null && !moyenTransportSearch.getNumeroCommande().isEmpty())
      listCriteria.add(Criteria.where("numeroCommande").regex(".*"+moyenTransportSearch.getNumeroCommande().trim()+".*","i"));

    if (moyenTransportSearch != null && moyenTransportSearch.getEnService() != null && !moyenTransportSearch.getNumeroCommande().isEmpty())
      listCriteria.add(Criteria.where("enService").is(Boolean.valueOf(moyenTransportSearch.getEnService())));

    if (moyenTransportSearch != null && moyenTransportSearch.getNumeroCarteTotal() != null && !moyenTransportSearch.getNumeroCarteTotal().isEmpty())
      listCriteria.add(Criteria.where("numeroCarteTotal").regex(".*"+moyenTransportSearch.getNumeroCarteTotal().trim()+".*","i"));

    if (!listCriteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));

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
  public Collection<MoyenTransport> searchMoyenTransport(String searchValue) {
    final Query query = new Query();

    final List<Criteria> listCriteria = new ArrayList<>();

    if (searchValue == null || searchValue.isEmpty())
      return new ArrayList<>();

    listCriteria.add(Criteria.where("reference").regex(".*"+searchValue.trim()+".*","i"));
    listCriteria.add(Criteria.where("numeroSerie").regex(".*"+searchValue.trim()+".*","i"));
    listCriteria.add(Criteria.where("numeroCarteGrise").regex(".*"+searchValue.trim()+".*","i"));
    listCriteria.add(Criteria.where("matricule").regex(".*"+searchValue.trim()+".*","i"));

    query.addCriteria(new Criteria().orOperator(listCriteria.toArray(new Criteria[listCriteria.size()])));

    return mongoTemplate.find(query, MoyenTransport.class);
  }

  @Override
  public MoyenTransport createMoyenTransport(MoyenTransport moyenTransport) {
    String type = moyenTransport.getType().name().toLowerCase();

    if (moyenTransportDao.existsByReference(moyenTransport.getReference()))
      throw new IllegalArgumentException(String.format("Une %s avec cette référence existe déjà", type));

    if (moyenTransportDao.existsByNumeroSerie(moyenTransport.getNumeroSerie()))
      throw new IllegalArgumentException(String.format("Une %s avec ce numéro de série existe déjà", type));

    if (moyenTransportDao.existsByNumeroCarteGrise(moyenTransport.getNumeroCarteGrise()))
      throw new IllegalArgumentException(String.format("Une %s avec ce numéro carte grise existe déjà", type));

    if (moyenTransportDao.existsByMatricule(moyenTransport.getMatricule()))
      throw new IllegalArgumentException(String.format("Une %s avec cette matricule existe déjà", type));

    moyenTransport.setDateCreation(LocalDateTime.now());

    moyenTransportDao.save(moyenTransport);

    return moyenTransport;
  }

  @Override
  public MoyenTransport updateMoyenTransport(String id, MoyenTransport moyenTransport) {
    Optional<MoyenTransport> moyenTransportExist = moyenTransportDao.findById(id);

    if (!moyenTransportExist.isPresent())
      throw new IllegalArgumentException("Ce moyen de transport n'existe pas");

    String type = moyenTransport.getType().name().toLowerCase();

    if (!moyenTransportExist.get().getReference().equals(moyenTransport.getReference()) &&
      moyenTransportDao.existsByReference(moyenTransport.getReference()))
      throw new IllegalArgumentException(String.format("Une %s avec cette référence existe déjà", type));

    if (!moyenTransportExist.get().getNumeroSerie().equals(moyenTransport.getNumeroSerie()) &&
      moyenTransportDao.existsByNumeroSerie(moyenTransport.getNumeroSerie()))
      throw new IllegalArgumentException(String.format("Une %s avec ce numéro de série existe déjà", type));

    if (!moyenTransportExist.get().getNumeroCarteGrise().equals(moyenTransport.getNumeroCarteGrise()) &&
      moyenTransportDao.existsByNumeroCarteGrise(moyenTransport.getNumeroCarteGrise()))
      throw new IllegalArgumentException(String.format("Une %s avec ce numéro carte grise existe déjà", type));

    if (!moyenTransportExist.get().getMatricule().equals(moyenTransport.getMatricule()) &&
      moyenTransportDao.existsByMatricule(moyenTransport.getMatricule()))
      throw new IllegalArgumentException(String.format("Une %s avec cette matricule existe déjà", type));

    moyenTransport.setId(id);

    moyenTransportDao.save(moyenTransport);

    return moyenTransport;
  }

  @Override
  public void deleteMoyenTransport(String id) {
    Optional<MoyenTransport> moyenTransportExist = moyenTransportDao.findById(id);

    if (!moyenTransportExist.isPresent())
      throw new IllegalArgumentException("Ce moyen de transport n'existe pas");

    MoyenTransport moyenTransport = moyenTransportExist.get();

    if (moyenTransport.isEnService())
      throw new IllegalArgumentException("Ce moyen de transport est en service");

    moyenTransportDao.delete(moyenTransport);
  }
}
