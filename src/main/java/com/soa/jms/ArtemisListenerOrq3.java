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
import com.soa.dto.RespuestaCobroTotal;
import com.soa.dto.RespuestaDatosTiempo;

/**
 * Class for receiving messages in an artemis queue
 */
@Component
public class ArtemisListenerOrq3 {

    @Autowired
    private PeliculasBusiness business;

    @Autowired
    private JmsSender sender;

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.cargo.in}")
    private String outQueueName;

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.fail.out}")
    private String outQueueFail;

    @JmsListener(destination = "${queue.cobroTotal.in}")
    public void receive(String message) {
//        System.out.println(String.format("Mensaje recibido del orq 2: %s",
//                message));
        Gson gson = new Gson();
        RespuestaDatosTiempo datos = gson.fromJson(message, RespuestaDatosTiempo.class);
        RespuestaCobroTotal respuesta = business.validacionSaldo(datos);
//        System.out.println("resultado de la consulta tiempo: " + respuesta);
        try {
            if ("Error en BD al consultar el saldo de la tarjeta".equals(respuesta.getMessage()) ||
                    ("No se puede realizar el cobro, a cobrar: " + respuesta.getCobro() + ", saldo: "
                            + respuesta.getTarjetas().get(0).getSaldoDisponible()).equals(respuesta.getMessage())){
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
