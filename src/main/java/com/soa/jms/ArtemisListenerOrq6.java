/**
 * 
 */
package com.soa.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.soa.business.PeliculasBusiness;
import com.soa.dto.RespuestaInicioStream;
import com.soa.dto.RespuestaStream;

/**
 * Class for receiving messages in an artemis queue
 */
@Component
public class ArtemisListenerOrq6 {

    @Autowired
    private PeliculasBusiness business;

    @Autowired
    private JmsSender sender;

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.inicioStream.out}")
    private String outQueueName;
    
    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.fail.out}")
    private String outQueueFail;
    
    @JmsListener(destination = "${queue.inicioStream.in}")
    public void receive(String message) {
//        System.out.println(String.format("Mensaje recibido del orq 5: %s",
//                message));
        Gson gson = new Gson();
        RespuestaStream datos = gson.fromJson(message, RespuestaStream.class);
        
        /* Validar si la pelicula existe en el catalogo */
        RespuestaInicioStream respuesta = business.inicioStream(datos);
//        System.out.println("resultado de la consulta tiempo: " + respuesta);
        try {
            if ("No se pudo iniciar el stream.".equals(respuesta.getMessage())) {
                System.out.println(respuesta.toString());
                sender.sendMessage(respuesta.toString(), outQueueFail);
            } else {
                sender.sendMessage(respuesta.toString(), outQueueName);
                System.out.println(String.format("Mensaje enviado Orq3: %s",
                        respuesta.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
