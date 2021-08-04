package com.safelogisitics.gestionentreprisesusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.safelogisitics.gestionentreprisesusers.dao.EntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeEntreprise;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypePartenariat;
import com.safelogisitics.gestionentreprisesusers.payload.request.EntrepriseProspectRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.EntrepriseRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EntrepriseServiceImpl implements EntrepriseService {

  @Autowired
  EntrepriseDao entrepriseDao;

  @Autowired
  InfosPersoService infosPersoService;

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public Page<Entreprise> getEntreprises(ETypeEntreprise typeEntreprise, Set<ETypePartenariat> typePartenariats, String agentId, String denomination, String ninea, Pageable pageable) {
    final Query query = new Query().with(pageable);
    final List<Criteria> criteria = new ArrayList<>();

    if (typeEntreprise != null)
      criteria.add(Criteria.where("typeEntreprise").is(typeEntreprise));

    if (typePartenariats != null && !typePartenariats.isEmpty())
      criteria.add(Criteria.where("typePartenariats").in(typePartenariats));

    if (agentId != null && !agentId.isEmpty())
      criteria.add(Criteria.where("agentId").is(agentId));
    
    if (denomination != null && !denomination.isEmpty())
      criteria.add(Criteria.where("denomination").is(denomination));

    if (ninea != null && !ninea.isEmpty())
      criteria.add(Criteria.where("ninea").is(ninea));

    criteria.add(Criteria.where("deleted").is(false));

    query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    List<Entreprise> listEntreprises = mongoTemplate.find(query, Entreprise.class);

    return PageableExecutionUtils.getPage(
      listEntreprises, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Entreprise.class));
  }

  @Override
  public Optional<Entreprise> getEntrepriseById(String id) {
    return entrepriseDao.findById(id);
  }

  @Override
  public Entreprise createEntreprise(EntrepriseRequest request) {
    if (entrepriseDao.existsByDenominationOrNineaAndDeletedIsFalse(request.getDenomination(), request.getNinea())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette entreprise existe déjà!");
    }

    Entreprise entreprise = new Entreprise(request.getDenomination(), request.getNinea(), request.getRaisonSociale(), request.getEmail(), request.getTelephone(), request.getAdresse());
    InfosPerso gerant = infosPersoService.createInfosPerso(request.getGerant());
    entreprise.setGerantId(gerant.getId());
    entrepriseDao.save(entreprise);

    return entreprise;
  }

  @Override
  public Entreprise createEntreprise(EntrepriseProspectRequest request) {
    if (entrepriseDao.existsByDenominationOrNineaAndDeletedIsFalse(request.getDenomination(), request.getNinea())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette entreprise existe déjà!");
    }

    Entreprise entreprise = new Entreprise(request.getDenomination(), request.getNinea(), request.getRaisonSociale(), request.getEmail(), request.getTelephone(), request.getAdresse());

    entrepriseDao.save(entreprise);

    return entreprise;
  }

  @Override
  public Entreprise updateEntreprise(String id, EntrepriseRequest request) {
    Optional<Entreprise> _entreprise = entrepriseDao.findById(id);

    if (!_entreprise.isPresent() || _entreprise.get().isDeleted()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entreprise avec cette id n'existe pas!");
    }

    Entreprise entreprise = _entreprise.get();

    entreprise.setTypeEntreprise(request.getTypeEntreprise());
    entreprise.setTypePartenariats(request.getTypePartenariats());
    entreprise.setDenomination(request.getDenomination());
    entreprise.setNinea(request.getNinea());
    entreprise.setRaisonSociale(request.getRaisonSociale());
    entreprise.setDenomination(request.getDenomination());
    entreprise.setEmail(request.getEmail());
    entreprise.setTelephone(request.getTelephone());
    entreprise.setAdresse(request.getAdresse());

    InfosPerso gerant = infosPersoService.createInfosPerso(request.getGerant());
    entreprise.setGerantId(gerant.getId());

    entrepriseDao.save(entreprise);

    return entreprise;
  }

  @Override
  public void deleteEntreprise(String id) {
    Optional<Entreprise> _entreprise = entrepriseDao.findById(id);

    if (_entreprise.isPresent()) {
      Entreprise entreprise = _entreprise.get();
      entreprise.setDeleted(true);
      entrepriseDao.save(entreprise);
    }
  }
}
