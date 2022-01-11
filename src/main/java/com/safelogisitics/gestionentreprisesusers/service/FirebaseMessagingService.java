package com.safelogisitics.gestionentreprisesusers.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.safelogisitics.gestionentreprisesusers.data.model.PushNotification;

import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

  public String sendNotification(PushNotification pushNotification) throws FirebaseMessagingException {

    Notification notification = Notification
      .builder()
      .setTitle(pushNotification.getTitle())
      .setBody(pushNotification.getMessage())
      .build();

    Message message = Message
      .builder()
      .setTopic(pushNotification.getTopic())
      .putAllData(pushNotification.getData())
      .setNotification(notification)
      .build();

    return FirebaseMessaging.getInstance().send(message);
  }
}