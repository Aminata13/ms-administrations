package com.safelogisitics.gestionentreprisesusers.web.kafkaListener;

import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.SmsColisLivreDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SendSmsRequest;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.service.SMSService;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SmsColisLivreListener {

    private SMSService smsService;
    private MongoTemplate mongoTemplate;

    public SmsColisLivreListener(SMSService smsService, MongoTemplate mongoTemplate) {
        this.smsService = smsService;
        this.mongoTemplate = mongoTemplate;
    }

    @KafkaListener(topics = "${kafka.topics.smsColisLivre.name}", containerFactory = "smsColisLivreKafkaListenerContainerFactory", autoStartup = "${kafka.enabled}")
    public void listenSmsColisLivre(SmsColisLivreDto smsColisLivreDto) {
        final List<AggregationOperation> aggregations = new ArrayList<AggregationOperation>();
        final List<Criteria> criterias = new ArrayList<>();

        if(smsColisLivreDto.getParticulierId() != null) criterias.add(Criteria.where("_id").is(smsColisLivreDto.getParticulierId()));
        if(smsColisLivreDto.getEntrepriseId() != null) criterias.add(Criteria.where("entrepriseId").is(smsColisLivreDto.getEntrepriseId()));
        criterias.add(Criteria.where("deleted").is(false));
        criterias.add(Criteria.where("statut").ne(-1));

        aggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
        aggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
        aggregations.add(Aggregation.unwind("userInfos"));
        aggregations.add(Aggregation.match(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]))));

        Aggregation aggregation = Aggregation.newAggregation(aggregations);

        Compte compte = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getUniqueMappedResult();

        if (compte != null && compte.getId() != null) {
            String smsText = String.format("Bonjour M/Mme %s, votre colis n°%s vient d'être livré.\nPour des réclamations, veuillez saisir le service commercial.\nSafelogistics vous remercie.\nService commercial : 78 306 45 45",
                    compte.getUserInfos().getNomComplet(), smsColisLivreDto.getNumeroCommande());

            SendSmsRequest sms = new SendSmsRequest("RAK IN TAK", "Colis livre", smsText, Arrays.asList(compte.getUserInfos().getTelephone()));

            smsService.sendSms(sms);
        }
    }
}
