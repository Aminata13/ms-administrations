package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Collection;
import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.model.TypeEvenement;

public interface TypeEvenementService {
  
  public Collection<TypeEvenement> getTypeEvenements();

  public Optional<TypeEvenement> getTypeEvenementById(String id);

  public TypeEvenement createTypeEvenement(TypeEvenement typeEvenement);

  public TypeEvenement updateTypeEvenement(String id, TypeEvenement typeEvenement);

  public void deleteTypeEvenement(String id);
}
