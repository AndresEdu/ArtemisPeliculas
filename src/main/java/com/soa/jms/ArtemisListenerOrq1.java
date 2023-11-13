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
import com.soa.dto.JsonInit;
import com.soa.dto.RespuestaValidacionPelicula;

/**
 * Class for receiving messages in an artemis queue.
 */
@Component
public class ArtemisListenerOrq1 {

    @Autowired
    private PeliculasBusiness business;

    @Autowired
    private JmsSender sender;

    /** Nombre de la cola de respuesta del microservicio. */
    @Value("${queue.cobro.in}")
    private String outQueueName;
    
    /* Nombre de la cola de respuesta del microservicio*/
    @Value("${queue.fail.out}")
    private String outQueueFail;
    

    @JmsListener(destination = "${queue.name.in}")
    public void receive(String message) {
        System.out.println(String.format("Mensaje Recibido: %s",
                message));
        Gson gson = new Gson();
        JsonInit datos = gson.fromJson(message, JsonInit.class);
        RespuestaValidacionPelicula respuesta = business.validacionExistencia(datos);
        //System.out.println("resultado de la consulta: " + respuesta);
        
        try {
            if ("La pelicula no esta en el catalogo".equals(respuesta.getMessage())) {
                System.out.println(respuesta.toString());
                sender.sendMessage(respuesta.toString(), outQueueFail);
            }else {
            sender.sendMessage(respuesta.toString(), outQueueName);
            System.out.println(String.format("Mensaje enviado Orq1: %s", 
                    respuesta.toString()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
