package com.safelogisitics.gestionentreprisesusers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.EntrepriseDao;
import com.safelogisitics.gestionentreprisesusers.data.dao.NumeroCarteDao;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.AbonnementRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.EntrepriseProspectRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.EntrepriseRequest;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.InfosPersoAvecCompteRequest;
import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.data.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.data.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.data.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.data.model.NumeroCarte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  private EntrepriseDao entrepriseDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private NumeroCarteDao numeroCarteDao;

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private AbonnementService abonnementService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Page<Entreprise> getEntreprises(String typeEntreprise, String domaineActivite, String agentId, String denomination, String ninea, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "dateCreation"));
    final List<Criteria> criteria = new ArrayList<>();

    if (typeEntreprise != null)
      criteria.add(Criteria.where("typeEntreprise").is(typeEntreprise));

    if (domaineActivite != null && !domaineActivite.isEmpty())
      criteria.add(Criteria.where("domaineActivite").is(domaineActivite));

    if (agentId != null && !agentId.isEmpty())
      criteria.add(Criteria.where("agentId").is(agentId));

    if (denomination != null && !denomination.isEmpty())
      criteria.add(Criteria.where("denomination").regex(".*"+denomination+".*", "i"));

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
    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(request.getNumeroCarte());

    if (entrepriseDao.existsByDenominationAndDeletedIsFalse(request.getDenomination()))
      throw new IllegalArgumentException("Cette entreprise existe déjà!");

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (numeroCarteExist.get().isActive())
      throw new IllegalArgumentException("Cette carte est déjà activé!");

    if (!numeroCarteExist.get().getNumero().startsWith("20210030"))
      throw new IllegalArgumentException("Cette carte ne correspond pas avec les cartes entreprises!");

    NumeroCarte carte = numeroCarteExist.get();

    Entreprise entreprise = new Entreprise(request.getTypeEntreprise(), request.getDomaineActivite(), request.getDenomination(), request.getNinea(), request.getRaisonSociale(), request.getEmail(), request.getTelephone(), request.getAdresse());
    entrepriseDao.save(entreprise);

    InfosPersoAvecCompteRequest infosAgentRequest = objectMapper.convertValue(request.getGerant(), InfosPersoAvecCompteRequest.class);
    infosAgentRequest.setEntrepriseId(entreprise.getId());
    infosAgentRequest.setStatut(1);
    InfosPerso gerant = infosPersoService.createOrUpdateCompteEntreprise(null, infosAgentRequest);
    Compte compte = compteDao.findByInfosPersoIdAndType(gerant.getId(), ECompteType.COMPTE_ENTREPRISE).get();

    entreprise.setGerantId(compte.getId());
    entrepriseDao.save(entreprise);

    abonnementService.createAbonnement(
      new AbonnementRequest(carte.getTypeAbonnementId(), null, entreprise.getId(), 1, carte.getNumero(), false),
      ECompteType.COMPTE_ADMINISTRATEUR
    );

    entreprise.setNumeroCarte(carte.getNumero());
    entrepriseDao.save(entreprise);

    return entreprise;
  }

  @Override
  public Entreprise createEntreprise(EntrepriseProspectRequest request) {
    if (entrepriseDao.existsByDenominationAndDeletedIsFalse(request.getDenomination())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette entreprise existe déjà!");
    }

    Entreprise entreprise = new Entreprise(request.getTypeEntreprise(), request.getDomaineActivite(), request.getDenomination(), request.getNinea(), request.getRaisonSociale(), request.getEmail(), request.getTelephone(), request.getAdresse());
    entreprise.setDomaineActivite(request.getDomaineActivite());

    entrepriseDao.save(entreprise);

    return entreprise;
  }

  @Override
  public Entreprise updateEntreprise(String id, EntrepriseRequest request) {
    Optional<Entreprise> _entreprise = entrepriseDao.findById(id);

    if (!_entreprise.isPresent() || _entreprise.get().isDeleted())
      throw new IllegalArgumentException("Cette entreprise n'existe pas ou a été supprimé!");

    Entreprise entreprise = _entreprise.get();

    entreprise.setTypeEntreprise(request.getTypeEntreprise());
    entreprise.setDomaineActivite(request.getDomaineActivite());
    entreprise.setTypePartenariats(request.getTypePartenariats());
    entreprise.setDenomination(request.getDenomination());
    entreprise.setNinea(request.getNinea());
    entreprise.setRaisonSociale(request.getRaisonSociale());
    entreprise.setEmail(request.getEmail());
    entreprise.setTelephone(request.getTelephone());
    entreprise.setAdresse(request.getAdresse());

    InfosPersoAvecCompteRequest infosAgentRequest = objectMapper.convertValue(request.getGerant(), InfosPersoAvecCompteRequest.class);
    infosAgentRequest.setEntrepriseId(entreprise.getId());
    id = null;
    if (entreprise.getGerantId() != null  && compteDao.existsById(entreprise.getGerantId())) {
      id = compteDao.findById(entreprise.getGerantId()).get().getInfosPersoId();
    }
    InfosPerso gerant = infosPersoService.createOrUpdateCompteEntreprise(id, infosAgentRequest);
    Compte compte = compteDao.findByInfosPersoIdAndType(gerant.getId(), ECompteType.COMPTE_ENTREPRISE).get();

    entreprise.setGerantId(compte.getId());
    entrepriseDao.save(entreprise);

    Optional<NumeroCarte> numeroCarteExist = numeroCarteDao.findByNumero(request.getNumeroCarte());

    if (!numeroCarteExist.isPresent())
      throw new IllegalArgumentException("Cette carte n'existe pas!");

    if (numeroCarteExist.get().isActive() && (entreprise.getNumeroCarte() == null || !entreprise.getNumeroCarte().equals(numeroCarteExist.get().getNumero())))
      throw new IllegalArgumentException("Cette carte est déjà activé!SA");

    if (!numeroCarteExist.get().getNumero().startsWith("20210030"))
      throw new IllegalArgumentException("Cette carte ne correspond pas avec les cartes entreprises!");

    NumeroCarte carte = numeroCarteExist.get();

    Optional<Abonnement> _abonnement = abonnementDao.findByEntrepriseId(entreprise.getId());

    if (!_abonnement.isPresent()) {
      abonnementService.createAbonnement(
        new AbonnementRequest(carte.getTypeAbonnementId(), null, entreprise.getId(), 1, carte.getNumero(), false),
        ECompteType.COMPTE_ADMINISTRATEUR
      );
    } else {
      Abonnement abonnement = _abonnement.get();
      if (!abonnement.getNumeroCarte().equals(request.getNumeroCarte())) {
        NumeroCarte oldCarte = numeroCarteDao.findByNumero(abonnement.getNumeroCarte()).get();

        abonnement.setNumeroCarte(request.getNumeroCarte());
        abonnementDao.save(abonnement);

        oldCarte.setActive(false);
        carte.setActive(true);
        numeroCarteDao.save(oldCarte);
        numeroCarteDao.save(carte);
      }
    }

    entreprise.setNumeroCarte(carte.getNumero());
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
