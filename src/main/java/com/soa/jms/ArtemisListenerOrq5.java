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
import com.soa.dto.RespuestaStream;
import com.soa.dto.RespuestaStreamInit;

/**
 * Class for receiving messages in an artemis queue
 */
@Component
public class ArtemisListenerOrq5 {

    @Autowired
    private PeliculasBusiness business;

    @Autowired
    private JmsSender sender;

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.inicioStream.in}")
    private String outQueueName;

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.fail.out}")
    private String outQueueFail;

    @JmsListener(destination = "${queue.stream.in}")
    public void receive(String message) {
//        System.out.println(String.format("Mensaje recibido del orq 4: %s",
//                message));
        Gson gson = new Gson();
        RespuestaStreamInit datos = gson.fromJson(message, RespuestaStreamInit.class);
        /* Validar si la pelicula existe en el catalogo */
        RespuestaStream respuesta = business.validacionStream(datos);
//        System.out.println("resultado de la consulta tiempo: " + respuesta);
        try {
            if ("Error en BD al iniciar el stream".equals(respuesta.getMessage()) ||
                    "No se pudo iniciar el stream, porque no se realizo el pago".equals(respuesta.getMessage())) {
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
