package com.safelogisitics.gestionentreprisesusers.data.shared.repository.custom.impl;

import java.util.ArrayList;
import java.util.List;

import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoSearchRequestDto;
import com.safelogisitics.gestionentreprisesusers.data.shared.model.SharedInfosPersoModel;
import com.safelogisitics.gestionentreprisesusers.data.shared.repository.custom.SharedInfosPersoRepositoryCustom;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class SharedInfosPersoRepositoryCustomImpl implements SharedInfosPersoRepositoryCustom {

  @Autowired
  @Qualifier(value = "sharedMongoTemplate")
  private MongoTemplate sharedMongoTemplate;

  public SharedInfosPersoRepositoryCustomImpl(MongoTemplate sharedMongoTemplate) {
    this.sharedMongoTemplate = sharedMongoTemplate;
  }

  @Override
  public Page<SharedInfosPersoModel> customSearch(InfosPersoSearchRequestDto infosPersoSearch, Pageable pageable) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<>();

    listAggregations.add(Aggregation.sort(Sort.Direction.DESC, "dateCreation"));
    listAggregations.add(l -> new Document("$addFields", new Document("stringId", new Document("$toString", "$_id"))));

    listCritarias.addAll(infosPersoSearch.handleSearchParameters());

    if (infosPersoSearch.getCompteSearch() != null) {
      listAggregations.add(Aggregation.lookup("comptes", "stringId", "infosPersoId", "compte"));
      listAggregations.add(Aggregation.unwind("compte"));
      listCritarias.addAll(infosPersoSearch.getCompteSearch().handleSearchParameters());
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

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    Document aggregationResult = sharedMongoTemplate.aggregate(aggregation, SharedInfosPersoModel.class, Document.class)
      .getUniqueMappedResult();
    
    if (aggregationResult == null)
      return new PageImpl<>(new ArrayList<>(), pageable, 0);

    return new PageImpl<>(
      aggregationResult.getList("infosPersos", SharedInfosPersoModel.class),
      pageable,
      aggregationResult.getInteger("count")
    );
  }

}