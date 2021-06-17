package com.safelogisitics.gestionentreprisesusers.dao.jsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.safelogisitics.gestionentreprisesusers.model.Compte;

import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CompteJsonSerializer extends JsonSerializer<Compte> {

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
        jsonGenerator.writeStringField("numeroEmei", compte.getNumeroEmei());
        jsonGenerator.writeStringField("numeroReference", compte.getNumeroReference());
        jsonGenerator.writeNumberField("statut", compte.getStatut());
        jsonGenerator.writeObjectField("services", compte.getServices());

        jsonGenerator.writeEndObject(); 
      }
  }
}