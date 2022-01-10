package com.safelogisitics.gestionentreprisesusers.service;

import com.safelogisitics.gestionentreprisesusers.model.ConsommationCarburant;
import com.safelogisitics.gestionentreprisesusers.payload.request.ConsommationCarburantRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsommationCarburantService {

    public Page<ConsommationCarburant> getConsommationCarburants(String dateDebut, String dateFin, String moyenTransportId, Pageable pageable);

    public ConsommationCarburant addConsommationCarburant(ConsommationCarburantRequest consommationCarburantRequest);

    public ConsommationCarburant updateConsommationCarburant(String id, ConsommationCarburantRequest consommationCarburantRequest);
}
