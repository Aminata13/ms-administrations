package com.safelogisitics.gestionentreprisesusers.data.repository.custom.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAggregationDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.repository.custom.InfosPersoRepositoryCustom;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class InfosPersoRepositoryCustomImpl implements InfosPersoRepositoryCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<InfosPersoModel> customSearch(InfosPersoSearchRequestDto infosPersoSearch, Pageable pageable) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<>();

    listAggregations.add(Aggregation.sort(Sort.Direction.DESC, "dateCreation"));
    listAggregations.add(Aggregation.addFields().addFieldWithValue("stringId", ConvertOperators.ToString.toString("$_id")).build());
    // listAggregations.add(l -> new Document("$addFields", new Document("stringId", new Document("$toString", "$_id"))));

    listCritarias.addAll(infosPersoSearch.handleSearchParameters());

    if (infosPersoSearch.getCompteSearch() != null) {
      listAggregations.add(Aggregation.lookup("comptes", "stringId", "infosPersoId", "compte"));
      listAggregations.add(Aggregation.unwind("compte"));
      // listAggregations.add(Aggregation.group("id").addToSet("compte").as("compte"));
      // listAggregations.add(Aggregation.unwind("comptes"));
      listCritarias.addAll(infosPersoSearch.getCompteSearch().handleSearchParameters());
      // listAggregations.add(l -> new Document("$group", new Document("_id", Arrays.asList(
      //   new Document("compte", "$compte")
      // ))));
    }

    if (infosPersoSearch.getAbonnementSearch() != null) {
      listAggregations.add(Aggregation.lookup("abonnements", "stringId", "compteClient.infosPersoId", "abonnement"));
      listAggregations.add(Aggregation.unwind("abonnement"));
      listCritarias.addAll(infosPersoSearch.getAbonnementSearch().handleSearchParameters());
    }

    if (!listCritarias.isEmpty())
      listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    listAggregations.add(new SkipOperation(pageable.getPageNumber() * pageable.getPageSize()));
    listAggregations.add(Aggregation.limit(pageable.getPageSize()));

    listAggregations.add(Aggregation.group("id").addToSet(Aggregation.ROOT).as("infosPersos").count().as("count"));
    // listAggregations.add(Aggregation.group("id").addToSet("compte").as("compte"));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    List<Document> aggregationResult = mongoTemplate.aggregate(aggregation, InfosPersoModel.class, Document.class).getMappedResults();
    System.out.println(aggregationResult);
    return new PageImpl<>(new ArrayList<>(), pageable, 0);

    // if (aggregationResult == null)
    //   return new PageImpl<>(new ArrayList<>(), pageable, 0);

    // return new PageImpl<>(aggregationResult.getInfosPersos(), pageable, aggregationResult.getCount());
  }

}


/* 
if (infosPersoSearch.getCompteSearch() != null) {
      listAggregations.add(Aggregation.lookup("comptes", "_infosPersoId", "infosPersoId", "comptes"));
      listAggregations.add(Aggregation.unwind("compte"));
      listCritarias.addAll(infosPersoSearch.getCompteSearch().handleSearchParameters());
      // listAggregations.add(l -> new Document("$group", new Document("_id", Arrays.asList(
      //   new Document("_id", "$_id"),
      //   new Document("type", "$type"),
      //   new Document("comptes", new Document("$mergeObjects", "$$ROOT.compte"))
      // ))));
      // listAggregations.add(Aggregation.group("id").addToSet("compte").as("comptes").push(Aggregation.ROOT).as("user"));
    }

    if (infosPersoSearch.getAbonnementSearch() != null) {
      listAggregations.add(Aggregation.lookup("abonnements", "_infosPersoId", "compteClient.infosPersoId", "abonnement"));
      listAggregations.add(Aggregation.unwind("abonnement"));
      listCritarias.addAll(infosPersoSearch.getAbonnementSearch().handleSearchParameters());
    }

    if (!listCritarias.isEmpty())
      listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    listAggregations.add(new SkipOperation(pageable.getPageNumber() * pageable.getPageSize()));
    listAggregations.add(Aggregation.limit(pageable.getPageSize()));

    listAggregations.add(Aggregation.group().addToSet(Aggregation.ROOT).as("infosPersos").count().as("count"));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    Document aggregationResult = mongoTemplate.aggregate(aggregation, InfosPersoModel.class, Document.class).getUniqueMappedResult();
    System.out.println(aggregationResult);
    return new PageImpl<>(new ArrayList<>(), pageable, 0);

    // if (aggregationResult == null)
    //   return new PageImpl<>(new ArrayList<>(), pageable, 0);

    // return new PageImpl<>(aggregationResult.getInfosPersos(), pageable, aggregationResult.getCount());
*/