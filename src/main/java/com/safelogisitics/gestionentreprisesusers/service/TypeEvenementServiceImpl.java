package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.safelogisitics.gestionentreprisesusers.dao.TypeEvenementDao;
import com.safelogisitics.gestionentreprisesusers.model.TypeEvenement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeEvenementServiceImpl implements TypeEvenementService {

  @Autowired
  private TypeEvenementDao typeEvenementDao;

  @Override
  public Collection<TypeEvenement> getTypeEvenements() {
    Iterable<TypeEvenement> typeEvenements = typeEvenementDao.findAll();
    return StreamSupport.stream(typeEvenements.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Optional<TypeEvenement> getTypeEvenementById(String id) {
    return typeEvenementDao.findById(id);
  }

  @Override
  public TypeEvenement createTypeEvenement(TypeEvenement typeEvenement) {
    if (typeEvenementDao.existsByLibelle(typeEvenement.getLibelle()))
      throw new IllegalArgumentException("Ce type d'événement existe déjà.");

    typeEvenementDao.save(typeEvenement);

    return typeEvenement;
  }

  @Override
  public TypeEvenement updateTypeEvenement(String id, TypeEvenement typeEvenement) {
    Optional<TypeEvenement> typeEvenementExist = typeEvenementDao.findById(id);

    if (!typeEvenementExist.isPresent())
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    if (!typeEvenementExist.get().getLibelle().equals(typeEvenement.getLibelle())  && typeEvenementDao.existsByLibelle(typeEvenement.getLibelle()))
      throw new IllegalArgumentException("Ce type d'événement existe déjà.");

    typeEvenement.setId(id);

    typeEvenementDao.save(typeEvenement);

    return typeEvenement;
  }

  @Override
  public void deleteTypeEvenement(String id) {
    Optional<TypeEvenement> typeEvenementExist = typeEvenementDao.findById(id);

    if (!typeEvenementExist.isPresent())
      throw new IllegalArgumentException("Ce type d'événement n'existe pas.");

    typeEvenementDao.delete(typeEvenementExist.get());
  }
}
