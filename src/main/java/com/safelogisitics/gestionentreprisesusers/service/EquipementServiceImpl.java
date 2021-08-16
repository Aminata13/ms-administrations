package com.safelogisitics.gestionentreprisesusers.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.dao.EquipementDao;
import com.safelogisitics.gestionentreprisesusers.model.Equipement;
import com.safelogisitics.gestionentreprisesusers.model.FournitureEquipement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class EquipementServiceImpl implements EquipementService {

  @Autowired
  private EquipementDao equipementDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Collection<Equipement> getEquipements(Map<String, String> parameters) {
    final Query query = new Query();
    final List<Criteria> criteria = new ArrayList<>();

    if (parameters != null && parameters.containsKey("categorie") && !parameters.get("categorie").isEmpty())
      criteria.add(Criteria.where("categories").is(parameters.get("libelle").trim().toUpperCase()) );

    if (parameters != null && parameters.containsKey("libelle") && !parameters.get("libelle").isEmpty())
      criteria.add(Criteria.where("libelle").regex(".*"+parameters.get("libelle").trim()+".*","i"));

    if (parameters != null && parameters.containsKey("fournisseur") && !parameters.get("fournisseur").isEmpty())
      criteria.add(Criteria.where("historiqueFournitures").elemMatch(Criteria.where("fournisseur").regex(".*"+parameters.get("fournisseur").trim()+".*","i")));

    if (parameters != null && parameters.containsKey("numeroCommande") && !parameters.get("numeroCommande").isEmpty())
      criteria.add(Criteria.where("historiqueFournitures").elemMatch(Criteria.where("numeroCommande").regex(".*"+parameters.get("numeroCommande").trim()+".*","i")));

    if (!criteria.isEmpty())
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

    return mongoTemplate.find(query, Equipement.class);
  }

  @Override
  public Optional<Equipement> getEquipementById(String id) {
    return equipementDao.findById(id);
  }

  @Override
  public Equipement createEquipement(Equipement equipement) {
    if (equipementDao.existsByLibelleAndCategories(equipement.getLibelle(), equipement.getCategories()))
      throw new IllegalArgumentException("Cet équipement existe déjà.");

    Equipement nouveauEquipement = new Equipement(equipement.getLibelle(), equipement.getCategories(), equipement.getDescription(), equipement.getSpecificites());
    nouveauEquipement.setStock(0);

    equipementDao.save(nouveauEquipement);

    return nouveauEquipement;
  }

  @Override
  public Equipement updateEquipement(String id, Equipement equipement) {
    Optional<Equipement> _editEquipement = equipementDao.findById(id);

    if (!_editEquipement.isPresent())
      throw new IllegalArgumentException("Cet équipement n'existe pas.");

    Optional<Equipement> equipementDataExist = equipementDao.findByLibelleAndCategories(equipement.getLibelle(), equipement.getCategories());

    if (equipementDataExist.isPresent() && !_editEquipement.get().getId().equals(equipementDataExist.get().getId()))
      throw new IllegalArgumentException("Cet équipement existe déjà.");

    Equipement editEquipement = _editEquipement.get();

    editEquipement.setLibelle(equipement.getLibelle());
    editEquipement.setCategories(equipement.getCategories());
    editEquipement.setDescription(equipement.getDescription());
    editEquipement.setSpecificites(equipement.getSpecificites());

    equipementDao.save(editEquipement);

    return editEquipement;
  }

  @Override
  public Equipement fournitureEquipement(String id, FournitureEquipement fourniture) {
    Optional<Equipement> _equipement = equipementDao.findById(id);

    if (!_equipement.isPresent())
      throw new IllegalArgumentException("Cet équipement n'existe pas.");

    if (equipementDao.existsByHistoriqueFournituresNumeroCommande(fourniture.getNumeroCommande()))
      throw new IllegalArgumentException("Une fourniture avec cette numéro de commande existe déjà.");

    Equipement equipement = _equipement.get();

    double stock = equipement.getStock() + fourniture.getQuantite();

    equipement.setStock(stock);

    FournitureEquipement nouveauFourniture = new FournitureEquipement(
      fourniture.getCout(),
      fourniture.getQuantite(),
      fourniture.getFournisseur(),
      fourniture.getNumeroCommande(),
      fourniture.getDateAchat()
    );

    equipement.addHistoriqueFourniture(nouveauFourniture);

    equipementDao.save(equipement);

    return equipement;
  }

  @Override
  public void deleteEquipement(String id) {
    Optional<Equipement> _equipement = equipementDao.findById(id);

    if (!_equipement.isPresent())
      throw new IllegalArgumentException("Cet équipement n'existe pas.");

    Equipement equipement = _equipement.get();

    if (equipement.getQuantiteAffecter() != 0)
      throw new IllegalArgumentException("Cet équipement est en cours d'utilisation.");

    equipementDao.delete(equipement);
  }
}
