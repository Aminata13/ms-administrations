package com.safelogisitics.gestionentreprisesusers.web.kafkaListener;

import com.safelogisitics.gestionentreprisesusers.data.dto.kafka.SmsCreateCommandeDto;
import com.safelogisitics.gestionentreprisesusers.data.dto.request.SendSmsRequest;
import com.safelogisitics.gestionentreprisesusers.data.enums.EServiceType;
import com.safelogisitics.gestionentreprisesusers.data.model.Compte;
import com.safelogisitics.gestionentreprisesusers.service.SMSService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SmsCreateCommandeListener {


    @Autowired
    private SMSService smsService;

    @Autowired
    MongoTemplate mongoTemplate;

    @KafkaListener(topics = "${kafka.topics.smsCreateCommande.name}", containerFactory = "smsCreateCommandeKafkaListenerContainerFactory", autoStartup = "${kafka.enabled}")
    public void listenSmsCreateCommande(SmsCreateCommandeDto smsCreateCommandeDto) {
        final List<AggregationOperation> listAggregations = new ArrayList<AggregationOperation>();
        final List<Criteria> listCritarias = new ArrayList<Criteria>(Arrays.asList(Criteria.where("_id").is(smsCreateCommandeDto.getClientId())));

        listCritarias.add(Criteria.where("deleted").is(false));
        listCritarias.add(Criteria.where("statut").ne(-1));

        listAggregations.add(l -> new Document("$addFields", new Document("infosPersoObjectId", new Document("$toObjectId", "$infosPersoId"))));
        listAggregations.add(Aggregation.lookup("infosPersos", "infosPersoObjectId", "_id", "userInfos"));
        listAggregations.add(Aggregation.unwind("userInfos"));
        listAggregations.add(Aggregation.match(new Criteria().andOperator(listCritarias.toArray(new Criteria[listCritarias.size()]))));

        Aggregation aggregation = Aggregation.newAggregation(listAggregations);

        Compte compte = mongoTemplate.aggregate(aggregation, Compte.class, Compte.class).getUniqueMappedResult();

        if (compte != null && compte.getId() != null) {
            SendSmsRequest sms = null;

            if (smsCreateCommandeDto.getService().equals(EServiceType.LIVRAISON)) {
                Long hours = Long.valueOf(smsCreateCommandeDto.getDuree().substring(0, 2));
                Long minutes = Long.valueOf(smsCreateCommandeDto.getDuree().substring(3));

                String debut = LocalTime.now().toString().substring(0, 5);
                String fin = LocalTime.now().plusHours(hours).plusMinutes(minutes).toString().substring(0, 5);

                String smsText = String.format("Bonjour M/Mme %s,\nVotre commande n° %s expédiée par %s à destination de %s a bien été enregistrée.\nVotre livraison sera effectuée entre %s et %s.\n Le code de retrait est %s.\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                        compte.getUserInfos().getNomComplet(), smsCreateCommandeDto.getNumeroCommande(), smsCreateCommandeDto.getExpediteur(), smsCreateCommandeDto.getDestinataire(), debut, fin, smsCreateCommandeDto.getCodeRetrait());

                sms = new SendSmsRequest("RAK IN TAK", "Confirmation commande", smsText, Arrays.asList(compte.getUserInfos().getTelephone()));
            } else {
                String smsText = String.format("Bonjour M/Mme %s,\nVotre commande n° %s a bien été enregistrée.\nNous vous contacterons ultérieurement par téléphone pour discuter des détails.\nSafelogistics vous remercie\nService commercial : 78 306 45 45",
                        compte.getUserInfos().getNomComplet(), smsCreateCommandeDto.getNumeroCommande());
                sms = new SendSmsRequest("RAK IN TAK", "Confirmation commande", smsText, Arrays.asList(compte.getUserInfos().getTelephone()));
            }

            smsService.sendSms(sms);
        }
    }
}
