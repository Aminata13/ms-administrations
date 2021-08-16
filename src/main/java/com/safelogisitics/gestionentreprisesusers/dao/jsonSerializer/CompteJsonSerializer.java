package com.safelogisitics.gestionentreprisesusers.dao.jsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.safelogisitics.gestionentreprisesusers.dao.EquipementDao;
import com.safelogisitics.gestionentreprisesusers.dao.MoyenTransportDao;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.Equipement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@JsonComponent
public class CompteJsonSerializer extends JsonSerializer<Compte> {

  @Autowired
  private MoyenTransportDao moyenTransportDao;

  @Autowired
  private EquipementDao equipementDao;

  @Override
  public void serialize(Compte compte, JsonGenerator jsonGenerator, 
    SerializerProvider serializerProvider) throws IOException, 
    JsonProcessingException {
      if (!compte.isDeleted()) {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", compte.getId());
        jsonGenerator.writeStringField("type", compte.getType().name());
        jsonGenerator.writeStringField("infosPersoId", compte.getInfosPersoId());
        jsonGenerator.writeStringField("entreprise", compte.getEntreprise() != null ? compte.getEntreprise().getId() : null);
        jsonGenerator.writeObjectField("role", compte.getCustomRoleFields());
        jsonGenerator.writeObjectField("moyenTransport", compte.getMoyenTransportId() != null ? moyenTransportDao.findById(compte.getMoyenTransportId()) : null);
        jsonGenerator.writeStringField("numeroEmei", compte.getNumeroEmei());
        jsonGenerator.writeStringField("numeroReference", compte.getNumeroReference());
        jsonGenerator.writeNumberField("statut", compte.getStatut());
        jsonGenerator.writeObjectField("services", compte.getServices());

        jsonGenerator.writeObjectField("equipements", compte.getEquipements().stream().map(affectationEquipement -> {
          Map<String, Object> customData = new HashMap<>();
          Equipement equipement = equipementDao.findById(affectationEquipement.getIdEquipement()).get();
          customData.put("id", equipement.getId());
          customData.put("libelle", equipement.getLibelle());
          customData.put("description", equipement.getDescription());
          customData.put("categories", equipement.getCategories());
          customData.put("specificites", affectationEquipement.getSpecificites());
          customData.put("quantite", affectationEquipement.getQuantite());
          return customData;
        }).collect(Collectors.toList()) );

        jsonGenerator.writeEndObject(); 
      }
  }
}