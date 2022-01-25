package com.safelogisitics.gestionentreprisesusers.data.fixture;

import java.util.Collection;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.data.dao.SMSModelDao;
import com.safelogisitics.gestionentreprisesusers.data.model.SMSModel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class SMSModelFixture implements CommandLineRunner {

  private SMSModelDao smsModelDao;

  private ObjectMapper objectMapper;

  public SMSModelFixture(SMSModelDao smsModelDao, ObjectMapper objectMapper) {
    this.smsModelDao = smsModelDao;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    Collection<SMSModel>  smsModels = objectMapper.readValue(getClass()
      .getResourceAsStream("/data/default-sms-models.json"), new TypeReference<Collection<SMSModel>>(){});

    for (SMSModel _smsModel: smsModels) {
      Optional<SMSModel> smsModelExist = smsModelDao.findByMotCleIgnoreCase(_smsModel.getMotCle());
      SMSModel smsModel = new SMSModel();
      if (smsModelExist.isPresent())
        smsModel = smsModelExist.get();

      smsModel.setSignature(_smsModel.getSignature());
      smsModel.setSubject(_smsModel.getSubject());
      smsModel.setContent(_smsModel.getContent());
      smsModel.setData(_smsModel.getData());
      smsModel.setCible(_smsModel.getCible());
      smsModel.setMotCle(_smsModel.getMotCle());
      smsModel.setRepetition(_smsModel.getRepetition());

      smsModelDao.save(smsModel);
    }
  }
}

