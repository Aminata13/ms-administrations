package com.safelogisitics.gestionentreprisesusers.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.InfosPersoDao;
import com.safelogisitics.gestionentreprisesusers.dao.ProspectDao;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Entreprise;
import com.safelogisitics.gestionentreprisesusers.model.InfosPerso;
import com.safelogisitics.gestionentreprisesusers.model.Prospect;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ETypeProspect;
import com.safelogisitics.gestionentreprisesusers.payload.request.ProspectRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.RegisterRequest;
import com.safelogisitics.gestionentreprisesusers.payload.response.UserInfosResponse;
import com.safelogisitics.gestionentreprisesusers.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProspectServiceImpl implements ProspectService {

  @Autowired
  private ProspectDao prospectDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private InfosPersoDao infosPersoDao;

  @Autowired
  private EntrepriseService entrepriseService;

  @Autowired
  private InfosPersoService infosPersoService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Page<Map<String, Object>> getProspects(Map<String,String> parameters, Pageable pageable) {
    final Query query = new Query().with(pageable).with(Sort.by(Sort.Direction.DESC, "dateCreation"));
    final List<Criteria> criteria = new ArrayList<>();

    // Rechercher dans infos general
    if (parameters != null && parameters.containsKey("type") && !parameters.get("type").isEmpty())
      criteria.add(Criteria.where("type").is(parameters.get("type")));

    if (parameters != null && parameters.containsKey("statutProspection") && !parameters.get("statutProspection").isEmpty())
      criteria.add(Criteria.where("statutProspection").is(Integer.valueOf(parameters.get("statutProspection"))));

    if (parameters != null && parameters.containsKey("prospecteurId") && !parameters.get("prospecteurId").isEmpty())
      criteria.add(Criteria.where("prospecteurId").is(parameters.get("prospecteurId")));

    if (parameters != null && parameters.containsKey("enroleurId") && !parameters.get("enroleurId").isEmpty())
      criteria.add(Criteria.where("enroleurId").is(parameters.get("enroleurId")));


    // Rechercher dans infos entreprise
    if (parameters != null && parameters.containsKey("infosEntreprise.typeEntreprise") && !parameters.get("infosEntreprise.typeEntreprise").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.typeEntreprise").is(parameters.get("infosEntreprise.typeEntreprise")));

    if (parameters != null && parameters.containsKey("infosEntreprise.denomination") && !parameters.get("infosEntreprise.denomination").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.denomination").regex(".*"+parameters.get("infosEntreprise.denomination").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.ninea") && !parameters.get("infosEntreprise.ninea").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.ninea").regex(".*"+parameters.get("infosEntreprise.ninea").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.email") && !parameters.get("infosEntreprise.email").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.email").regex(".*"+parameters.get("infosEntreprise.email").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.telephone") && !parameters.get("infosEntreprise.telephone").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.telephone").regex(".*"+parameters.get("infosEntreprise.telephone").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosEntreprise.adresse") && !parameters.get("infosEntreprise.adresse").isEmpty())
      criteria.add(Criteria.where("infosEntreprise.adresse").regex(".*"+parameters.get("infosEntreprise.adresse").trim()+".*","i"));


    // Recherche dans infos particulier 
    if (parameters != null && parameters.containsKey("infosParticulier.prenom") && !parameters.get("infosParticulier.prenom").isEmpty())
      criteria.add(Criteria.where("infosParticulier.prenom").regex(".*"+parameters.get("infosParticulier.prenom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.nom") && !parameters.get("infosParticulier.nom").isEmpty())
      criteria.add(Criteria.where("infosParticulier.nom").regex(".*"+parameters.get("infosParticulier.nom").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.email") && !parameters.get("infosParticulier.email").isEmpty())
      criteria.add(Criteria.where("infosParticulier.email").regex(".*"+parameters.get("infosParticulier.email").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("infosParticulier.telephone") && !parameters.get("infosParticulier.telephone").isEmpty())
      criteria.add(Criteria.where("infosParticulier.telephone").regex(".*"+parameters.get("infosParticulier.telephone").trim()+".*","i"));

    if (!criteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    List<Prospect> listprospects = mongoTemplate.find(query, Prospect.class);

    Page<Prospect> prospects = PageableExecutionUtils.getPage(
      listprospects, 
      pageable, 
      () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Prospect.class));

    return prospects.map(new Function<Prospect, Map<String, Object>>() {
      @Override
      public Map<String, Object> apply(Prospect prospect) {
        return getDefaultFields(prospect);
      }
    });
  }

  @Override
  public Map<String, Object> getProspectById(String id) {
    Optional<Prospect> prospect = prospectDao.findById(id);
    if (!prospect.isPresent()) {
      return new LinkedHashMap<>();
    }
    return getDefaultFields(prospect.get());
  }

  @Override
  public Map<String, Object> createProspect(ProspectRequest prospectRequest) {
    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      prospectDao.existsByInfosParticulierEmailOrInfosParticulierTelephone(prospectRequest.getInfosParticulier().getEmail(), prospectRequest.getInfosParticulier().getTelephone())
    ) throw new IllegalArgumentException("Ce particulier existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) &&
      (prospectDao.existsByInfosEntrepriseDenomination(prospectRequest.getInfosEntreprise().getDenomination()) ||
      (prospectRequest.getInfosEntreprise().getNinea() != null && prospectDao.existsByInfosEntrepriseNinea(prospectRequest.getInfosEntreprise().getNinea())))
    ) throw new IllegalArgumentException("Cette entreprise existe déjà");

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Compte prospecteur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    Prospect prospect = new Prospect(prospectRequest.getType(), prospecteur.getId(), prospectRequest.getNiveauAvancement(), prospectRequest.getRapportVisites());

    if (prospectRequest.getType().equals(ETypeProspect.PARTICULIER)) {
      if(prospectRequest.getInfosParticulier() == null)
        throw new IllegalArgumentException("Veuillez remplir les informations du client");

      prospect.setInfosParticulier(new InfosPerso(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        prospectRequest.getInfosParticulier().getDateNaissance()
      ));
    }

    if (prospectRequest.getType().equals(ETypeProspect.ENTREPRISE)) {
      if(prospectRequest.getInfosEntreprise() == null)
        throw new IllegalArgumentException("Veuillez remplir les informations de l'entreprise");

      prospect.setInfosEntreprise(new Entreprise(
        prospectRequest.getInfosEntreprise().getTypeEntreprise(),
        prospectRequest.getInfosEntreprise().getDomaineActivite(),
        prospectRequest.getInfosEntreprise().getDenomination(),
        prospectRequest.getInfosEntreprise().getNinea(),
        prospectRequest.getInfosEntreprise().getRaisonSociale(),
        prospectRequest.getInfosEntreprise().getEmail(),
        prospectRequest.getInfosEntreprise().getTelephone(),
        prospectRequest.getInfosEntreprise().getAdresse()
      ));
    }

    prospect.setPersonneRencontrer(prospectRequest.getPersonneRencontrer());

    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public Map<String, Object> updateProspect(String id, ProspectRequest prospectRequest) {
    Optional<Prospect> prospectExist = prospectDao.findById(id);
    if (!prospectExist.isPresent())
      throw new IllegalArgumentException("Ce prospect n'existe pas");

    Prospect prospect = prospectExist.get();

    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      !prospect.getInfosParticulier().getEmail().equals(prospectRequest.getInfosParticulier().getEmail()) &&
      prospectDao.existsByInfosParticulierEmail(prospectRequest.getInfosParticulier().getEmail())
    ) throw new IllegalArgumentException("Cette adresse email existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.PARTICULIER) &&
      !prospect.getInfosParticulier().getTelephone().equals(prospectRequest.getInfosParticulier().getTelephone()) &&
      prospectDao.existsByInfosParticulierTelephone(prospectRequest.getInfosParticulier().getTelephone())
    ) throw new IllegalArgumentException("Ce numéro de téléphone existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) &&
      !prospect.getInfosEntreprise().getDenomination().equals(prospectRequest.getInfosEntreprise().getDenomination()) &&
      prospectDao.existsByInfosEntrepriseDenomination(prospectRequest.getInfosEntreprise().getDenomination())
    ) throw new IllegalArgumentException("Cette dénomination existe déjà");

    if (
      prospectRequest.getType().equals(ETypeProspect.ENTREPRISE) && prospectRequest.getInfosEntreprise().getNinea() != null &&
      !prospect.getInfosEntreprise().getNinea().equals(prospectRequest.getInfosEntreprise().getNinea()) &&
      prospectDao.existsByInfosEntrepriseNinea(prospectRequest.getInfosEntreprise().getNinea())
    ) throw new IllegalArgumentException("Ce ninéa existe déjà");

    if (prospectRequest.getType().equals(ETypeProspect.PARTICULIER)) {
      prospect.setInfosParticulier(new InfosPerso(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        prospectRequest.getInfosParticulier().getDateNaissance()
      ));
    } else {
      prospect.setInfosEntreprise(new Entreprise(
        prospectRequest.getInfosEntreprise().getTypeEntreprise(),
        prospectRequest.getInfosEntreprise().getDomaineActivite(),
        prospectRequest.getInfosEntreprise().getDenomination(),
        prospectRequest.getInfosEntreprise().getNinea(),
        prospectRequest.getInfosEntreprise().getRaisonSociale(),
        prospectRequest.getInfosEntreprise().getEmail(),
        prospectRequest.getInfosEntreprise().getTelephone(),
        prospectRequest.getInfosEntreprise().getAdresse()
      ));
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Compte dernierEditeur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    prospect.setNiveauAvancement(prospectRequest.getNiveauAvancement());
    prospect.setPersonneRencontrer(prospectRequest.getPersonneRencontrer());
    prospect.setRapportVisites(prospectRequest.getRapportVisites());
    prospect.setDernierEditeurId(dernierEditeur.getId());
    prospect.setDateDerniereModification(LocalDateTime.now());
    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public Map<String, Object> enroleProspect(String id, ProspectRequest prospectRequest) {
    updateProspect(id, prospectRequest);
    Prospect prospect = prospectDao.findById(id).get();
    
    if (prospect.getType().equals(ETypeProspect.PARTICULIER)) {
      RegisterRequest registerRequest = new RegisterRequest(
        prospectRequest.getInfosParticulier().getPrenom(),
        prospectRequest.getInfosParticulier().getNom(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getEmail(),
        prospectRequest.getInfosParticulier().getTelephone(),
        prospectRequest.getInfosParticulier().getAdresse(),
        null, null, null, null
      );

      UserInfosResponse infosPerso = infosPersoService.createCompteClient(registerRequest);
      prospect.getInfosParticulier().setId(infosPerso.getId());
    }

    if (prospect.getType().equals(ETypeProspect.ENTREPRISE)) {
      Entreprise entreprise = entrepriseService.createEntreprise(prospectRequest.getInfosEntreprise());
      prospect.getInfosEntreprise().setId(entreprise.getId());
    }

    UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Compte enroleur = compteDao.findByInfosPersoIdAndType(currentUser.getInfosPerso().getId(), ECompteType.COMPTE_ADMINISTRATEUR).get();

    prospect.setStatutProspection(1);
    prospect.setEnroleurId(enroleur.getId());
    prospect.setDateEnrolement(LocalDateTime.now());

    prospectDao.save(prospect);

    return getDefaultFields(prospect);
  }

  @Override
  public void deleteProspect(String id) {
    prospectDao.deleteById(id);
  }

  private Map<String, Object>  getDefaultFields(Prospect prospect) {
    Map<String, Object> customFields = new LinkedHashMap<>();

    if (prospect == null) {
      return customFields;
    }

    customFields.put("id", prospect.getId());
    customFields.put("type", prospect.getType().name());
    customFields.put("statutProspection", prospect.getStatutProspection());
    customFields.put("rapportVisites", prospect.getRapportVisites());
    customFields.put("niveauAvancement", prospect.getNiveauAvancement());
    customFields.put("infosEntreprise", prospect.getInfosEntreprise());
    customFields.put("infosParticulier", prospect.getInfosParticulier());
    customFields.put("personneRencontrer", prospect.getPersonneRencontrer());
    customFields.put("prospecteur", infosPersoDao.findById(compteDao.findById(prospect.getProspecteurId()).get().getInfosPersoId()).get().getDefaultFields());
    customFields.put("dernierEditeur", prospect.getDernierEditeurId() != null ? infosPersoDao.findById(compteDao.findById(prospect.getDernierEditeurId()).get().getInfosPersoId()).get().getDefaultFields() : null);
    customFields.put("enroleur", prospect.getEnroleurId() != null ? infosPersoDao.findById(compteDao.findById(prospect.getEnroleurId()).get().getInfosPersoId()).get().getDefaultFields() : null);
    customFields.put("dateEnrolement", prospect.getDateEnrolement());
    customFields.put("dateDerniereModification", prospect.getDateDerniereModification());
    customFields.put("dateCreation", prospect.getDateCreation());

    return customFields;
  }
}
