package com.safelogisitics.gestionentreprisesusers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safelogisitics.gestionentreprisesusers.dao.AbonnementDao;
import com.safelogisitics.gestionentreprisesusers.dao.CompteDao;
import com.safelogisitics.gestionentreprisesusers.dao.SMSModelDao;
import com.safelogisitics.gestionentreprisesusers.model.Abonnement;
import com.safelogisitics.gestionentreprisesusers.model.Compte;
import com.safelogisitics.gestionentreprisesusers.model.SMSModel;
import com.safelogisitics.gestionentreprisesusers.model.enums.ECompteType;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSCible;
import com.safelogisitics.gestionentreprisesusers.model.enums.ESMSData;
import com.safelogisitics.gestionentreprisesusers.payload.request.SMSModelRequest;
import com.safelogisitics.gestionentreprisesusers.payload.request.SMSRequest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {
  
  private Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

  @Value("${sms.private_key}")
  private String privateKey;

  @Value("${sms.token}")
  private String token;

  @Value("${sms.apiUrl}")
	private String apiUrl;

  @Value("${sms.login}")
	private String login;

  @Autowired
  private SMSModelDao smsModelDao;

  @Autowired
  private CompteDao compteDao;

  @Autowired
  private AbonnementDao abonnementDao;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void sendSms(Set<SMSRequest> messages) {
    System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("externals/clienttrust").getPath());
    String documentJSON = handleSmsRequestsToJson(messages);
    // String documentJSON="{\"messages\":[{\"signature\": \"RAK IN TAK\",\"subject\": \"Validation paiement\",\"content\": \"TEST SMS: Bonjour votre paiement est validé\",\"recipients\": [{\"id\": \"1\",\"value\": \"221775919686\"}]}]}";
    String inputString = null;
    int responseCode = 0;
    String password = token;
    String authString = login + ":" + password;
    String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
    try {
      long timestamp = System.currentTimeMillis()/1000;
      String msgToEncrypt=token+documentJSON+timestamp;
      String key=hmacSha(privateKey, msgToEncrypt); // HMAC
      String URLAddress = apiUrl+"?token="+token+"&key="+key+"&timestamp="+timestamp;
      URL url = new URL(URLAddress);
      try {
        // Get an HttpURLConnection subclass object instead of URLConnection
        HttpsURLConnection myHttpConnection = (HttpsURLConnection) url.openConnection();
        myHttpConnection.setRequestMethod("POST");
        myHttpConnection.setDoOutput(true);
        myHttpConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        myHttpConnection.setRequestProperty("content-type", "application/json; charset=utf-8");
        //
        // Output the results
        OutputStream output = myHttpConnection.getOutputStream();
        output.write(documentJSON.toString().getBytes("UTF-8"));
        // output.write(queryParam.toString().getBytes("UTF-8"));
        output.flush();
        // get the response-code from the response
        responseCode = myHttpConnection.getResponseCode();
        if (responseCode == 401) {throw new RuntimeException("LOGIN ou TOKEN incorrect: Acces non autorise HTTP error code : "+ responseCode);}
        else if (responseCode == 400) {throw new RuntimeException("Erreur 103: Acces non autorise HTTP error code : "+ responseCode);}
        // open the contents of the URL as an inputStream and print to stdout
        BufferedReader in = new BufferedReader(new InputStreamReader(
        myHttpConnection.getInputStream()));
        while ((inputString = in.readLine()) != null) {
        System.out.println(inputString);
        logger.info(inputString);
        }
        in.close();
      } catch (IOException e) {
        logger.error(e.getMessage(), e.getCause());
        System.out.println(e.getMessage());
      }
    } catch (MalformedURLException e) {
      logger.error(e.getMessage(), e.getCause());
      System.out.println(e.getMessage());
    }
  }

  @Override
  public Page<SMSModel> getSMSModels(Map<String, String>parameters, Pageable pageable) {
    return smsModelDao.findAll(pageable);
  }

  @Override
  public Optional<SMSModel> getSMSModelById(String id) {
    return smsModelDao.findById(id);
  }

  @Override
  public SMSModel createSMSModel(SMSModelRequest smsModelRequest) {
    if (smsModelDao.existsBySubjectIgnoreCase(smsModelRequest.getSubject()))
      throw new IllegalArgumentException("Un modèle de sms avec ce même sujet existe déjà.");

    SMSModel smsModel = objectMapper.convertValue(smsModelRequest, SMSModel.class);

    smsModel.setMotCle(UUID.randomUUID().toString());

    smsModelDao.save(smsModel);

    return smsModel;
  }

  @Override
  public SMSModel updateSMSModel(String id, SMSModelRequest smsModelRequest) {
    Optional<SMSModel> _smsModel = smsModelDao.findById(id);

    if (!_smsModel.isPresent())
      throw new IllegalArgumentException("Ce modèle de sms n'existe pas.");

    SMSModel smsModel = _smsModel.get();
    String motCle = smsModel.getMotCle();

    if (!smsModel.getSubject().toLowerCase().equals(smsModelRequest.getSubject().toLowerCase()) && smsModelDao.existsBySubjectIgnoreCase(smsModelRequest.getSubject())) {
      throw new IllegalArgumentException("Un modèle de sms avec ce même sujet existe déjà.");
    }

    smsModel = objectMapper.convertValue(smsModelRequest, SMSModel.class);
    smsModel.setId(id);
    smsModel.setMotCle(motCle);

    smsModelDao.save(smsModel);

    return smsModel;
  }

  @Override
  public void deleteSMSModel(String id) {
    smsModelDao.deleteById(id);
  }

  @Override
  public Collection<String> getSMSData() {
    return Arrays.stream(ESMSData.values()).map(eValue -> eValue.name()).collect(Collectors.toList());
  }

  @Override
  public Collection<String> getSMSRepetitions() {
    return Arrays.stream(ESMSCible.values()).map(eValue -> eValue.name()).collect(Collectors.toList());
  }

  private static String hmacSha(String SECRETKEY, String VALUE) {
    try {
      SecretKeySpec signingKey = new SecretKeySpec(SECRETKEY.getBytes("UTF-8"), "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(signingKey);
      byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));
      byte[] hexArray = {
        (byte)'0', (byte)'1', (byte)'2', (byte)'3',
        (byte)'4', (byte)'5', (byte)'6', (byte)'7',
        (byte)'8', (byte)'9', (byte)'a', (byte)'b',
        (byte)'c', (byte)'d', (byte)'e', (byte)'f'
      };
    byte[] hexChars = new byte[rawHmac.length * 2];
      for ( int j = 0; j < rawHmac.length; j++ ) {
        int v = rawHmac[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private String handleSmsRequestsToJson(Set<SMSRequest> smsRequests) {
    String json = "";
    Collection<String> messages = new ArrayList<>();
    for (SMSRequest smsRequest : smsRequests) {
      Optional<SMSModel> smsModelExist = smsModelDao.findByMotCleIgnoreCase(smsRequest.getMotCle());
      if (!smsModelExist.isPresent() || (smsModelExist.get().getData() != null  && smsRequest.getData() == null)) {
        continue;
      }

      SMSModel smsModel = smsModelExist.get();
      
      Collection<Map<String, String>> smsRecipients = handleSmsRecipients(smsModel.getCible(), smsRequest.getRecipients());

      Collection<String> smsData = new ArrayList<>();
      if (smsModel.getData() != null && smsModel.getData().isEmpty()) {
        smsData = handleSmsData(smsModel.getData(), smsRequest.getData());
      }
      
      messages.add(smsModel.createSms(smsData, smsRecipients));
    }

    try {
      json = objectMapper.writeValueAsString(Collections.singletonMap("messages", messages));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return json;
  }

  private Collection<String> handleSmsData(Collection<ESMSData> dataSmsModel, Map<String, String> data) {
    Collection<String> smsData = new ArrayList<>();
    data.forEach((key, value) -> {
      if (key == "CLIENT_NOM_COMPLET" && dataSmsModel.contains(ESMSData.valueOf("CLIENT_NOM_COMPLET"))) {
        
      }
    });
    if (dataSmsModel.contains(ESMSData.CLIENT_NOM_COMPLET)) {
      if (!data.containsKey("clientId") || !compteDao.existsById(data.get("clientId"))) {
        return smsData;
      }
      Compte compteClient = compteDao.findById(data.get("clientId")).get();
      Optional<Abonnement> abonnement = abonnementDao.findByCompteClientId(compteClient.getId());
    }
    return smsData;
  }

  private Collection<Map<String, String>> handleSmsRecipients(ESMSCible cible, Set<String> data) {
    Collection<Map<String, String>> smsRecipients = new ArrayList<Map<String, String>>();
    Collection<String> recipients = new ArrayList<>();

    if (cible == ESMSCible.UNIQUE) {
      
    }

    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<Criteria>(Arrays.asList(
      Criteria.where("deleted").is(false), Criteria.where("statut").ne(-1)
    ));
    listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
    listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
    listAggregations.add(Aggregation.unwind("userInfos"));
    Aggregation aggregation = Aggregation.newAggregation();

    switch (cible) {
      case UNIQUE:
        recipients = data;
        break;

      case PARTICULIERS:
        listCritarias.add(Criteria.where("type").is(ECompteType.COMPTE_PARTICULIER));
        listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));
        aggregation = Aggregation.newAggregation(listAggregations);
        recipients = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getMappedResults()
          .stream().map(compte -> compte.getUserInfos().getTelephone()).collect(Collectors.toList());
        break;

      case COURSIERS:
        listCritarias.add(Criteria.where("type").is(ECompteType.COMPTE_COURSIER));
        listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));
        aggregation = Aggregation.newAggregation(listAggregations);
        recipients = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getMappedResults()
          .stream().map(compte -> compte.getUserInfos().getTelephone()).collect(Collectors.toList());
        break;

      case PRESTATAIRES:
        listCritarias.add(Criteria.where("type").is(ECompteType.COMPTE_PRESTATAIRE));
        listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));
        aggregation = Aggregation.newAggregation(listAggregations);
        recipients = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getMappedResults()
          .stream().map(compte -> compte.getUserInfos().getTelephone()).collect(Collectors.toList());
        break;

      default:
        break;
    }

    int index = 1;
    for (Iterator<String> value = recipients.iterator(); value.hasNext(); index++) {
      Map<String, String> recipient = new HashMap<>();
      recipient.put("id", String.valueOf(index));
      recipient.put("value", value.next());
      smsRecipients.add(recipient);
    }

    return smsRecipients;
  }

  private List<Compte> findComptes(ECompteType compteType, String compteId) {
    final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
    final List<Criteria> listCritarias = new ArrayList<Criteria>(Arrays.asList(
      Criteria.where("deleted").is(false),
      Criteria.where("statut").ne(-1)
    ));

    if (compteType != null)
      listCritarias.add(Criteria.where("type").is(compteType));

    if (compteId != null)
      listCritarias.add(Criteria.where("_id").is(compteId));

    listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
    listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
    listAggregations.add(Aggregation.lookup("abonnements", "infosPersoId", "compteClient.infosPersoId", "abonnement"));
    listAggregations.add(Aggregation.unwind("userInfos"));
    listAggregations.add(Aggregation.unwind("abonnement"));
    listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

    Aggregation aggregation = Aggregation.newAggregation(listAggregations);

    return mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getMappedResults();
  }
}
