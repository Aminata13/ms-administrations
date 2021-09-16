package com.safelogisitics.gestionentreprisesusers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
  
  private Logger logger = LoggerFactory.getLogger(SMSService.class);

  @Value("${sms.private_key}")
  private String privateKey;

  @Value("${sms.token}")
  private String token;

  @Value("${sms.apiUrl}")
	private String apiUrl;

  @Value("${sms.login}")
	private String login;

  /* 
  {
    messages: [
      {
        signature: "RAK IN TAK",
        subject: "Validation paiement",
        content: "TEST SMS: Bonjour votre paiement est validé",
        recipients: [{id: 1, value: "221776420768"}]
      }
    ]
  }
  //*/

  public void sendSms(/* Set<SMSMessageRequest> messages */) {
    System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("externals/clienttrust").getPath());
    String documentJSON="{\"messages\":[{\"signature\": \"RAK IN TAK\",\"subject\": \"Validation paiement\",\"content\": \"TEST SMS: Bonjour votre paiement est validé\",\"recipients\": [{\"id\": \"1\",\"value\": \"221775919686\"}]}]}";
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

  public static String hmacSha(String SECRETKEY, String VALUE) {
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

}
