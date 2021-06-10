package com.safelogisitics.gestionentreprisesusers.service;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  public JavaMailSender emailSender;

  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  public static int noOfQuickServiceThreads = 20;
	
	/**
	 * this statement create a thread pool of twenty threads
	 * here we are assigning send mail task using ScheduledExecutorService.submit();
	 */
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); 
  // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).

  @Override
  public void sendSimpleMessage(String to, String subject, String text) throws MailException, RuntimeException {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
          emailSender.send(message);
				}catch(Exception e){
					logger.error("Exception occur while send a mail : ",e);
				}
			}
		});
  }

  @Override
  public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) throws MailException, RuntimeException {
    MimeMessage message = emailSender.createMimeMessage();

    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text);
      
      FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
      helper.addAttachment("Invoice", file);
 
      quickService.submit(new Runnable() {
        @Override
        public void run() {
          try{
            emailSender.send(message);
          }catch(Exception e){
            logger.error("Exception occur while send a mail : ",e);
          }
        }
      });
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
