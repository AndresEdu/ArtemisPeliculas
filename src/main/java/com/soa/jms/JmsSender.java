package com.soa.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;

/**
 * Clase para enviar mensajes JMS.
 */
@Service
public class JmsSender {
    @Autowired
    JmsTemplate jmsTemplate;

    /**
     * Envia un mensajes jms a una cola.
     * @param message Mensaje a enviar.
     * @param queueName Nombre de la cola destino.
     */
    public void sendMessage(String message, String queueName) {
        this.jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
//        System.out.println(String.format("Mensaje enviado: %s", message));
    }
}


