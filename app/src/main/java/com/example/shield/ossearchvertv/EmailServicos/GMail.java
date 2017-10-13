package com.example.shield.ossearchvertv.EmailServicos;

/**
 * Created by Shield on 06/10/2017.
 */

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMail {

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";
    final String fromUser = "emailExemplo@gmail.com";
    final String fromUserEmailPassword = "123456";
    final String TO_EMAIL = "enviaos@emailsmtp.com.br";

    String fromTecnico;
    String fromPassword;
    String emailSubject = "Resumo da OS Digital";
    String emailBody;

//    String contra;
//    String nome;
//    String endereco;
//    String telCelular;
//    String telResidencial;
//    String telComercial;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public GMail() {

    }


    //public GMail(String fromTecnico, String fromPassword,
    //             List<String> toEmailList, String emailSubject, String emailBody) {
    public GMail(String fromTecnico, String emailBody) {
        this.fromTecnico = fromTecnico;
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(TO_EMAIL, "OS de " + fromTecnico));
        //for (String toEmail : toEmailList) {
        Log.i("GMail", "toEmail: " + TO_EMAIL);
        emailMessage.addRecipient(Message.RecipientType.TO,
                new InternetAddress(TO_EMAIL));
        //}

        emailMessage.setSubject(emailSubject);
        //emailMessage.setContent(emailBody,"text/html");// for a html email
        emailMessage.setText(emailBody, "utf-8", "html");// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        Log.i("INFO", emailHost);
        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        Log.i("GMail", "allrecipients: " + emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}
