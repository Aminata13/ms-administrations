package com.safelogisitics.gestionentreprisesusers.service;

import java.util.Optional;

import com.safelogisitics.gestionentreprisesusers.data.model.NumeroCarte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface NumeroCarteService {

  public Page<NumeroCarte> getNumeroCartes(String typeAbonnementId, Pageable pageable);

  public Optional<NumeroCarte> getNumeroCarteByNumero(String numero);

  public NumeroCarte createNumeroCarte(NumeroCarte numeroCarte);

  public Page<NumeroCarte> createNumeroCarte(MultipartFile file, String typeAbonnementId);

  public void deleteByNumero(String numeroCarte);
}
