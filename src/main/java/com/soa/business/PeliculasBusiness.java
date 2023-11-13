/**
 * 
 */
package com.soa.business;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.soa.dao.PeliculasDao;
import com.soa.dto.JsonInit;
import com.soa.dto.Pelicula;
import com.soa.dto.RespuestaCobroTotal;
import com.soa.dto.RespuestaDatosTiempo;
import com.soa.dto.RespuestaInicioStream;
import com.soa.dto.RespuestaStreamInit;
import com.soa.dto.RespuestaValidacionPelicula;
import com.soa.dto.Tarjeta;
import com.soa.jms.JmsSender;
import com.soa.dto.RespuestaStream;

/**
 * Clase para concatenación de datos personales.
 */
@Component
public class PeliculasBusiness {

    /** Objeto de acceso a datos. */
    @Autowired
    private PeliculasDao peliculasDao;

    /**
     * Metodo que regresa la respuesta y valida si existe la pelicula en el catalogo
     * 
     * @param pelicula
     * @return
     */
    public RespuestaValidacionPelicula validacionExistencia(JsonInit datos) {
        RespuestaValidacionPelicula respuesta = new RespuestaValidacionPelicula();
        try {
            List<Pelicula> list = peliculasDao.qryPeliculas(datos.getPelicula());
            if (list.isEmpty()) {
                respuesta.setMessage("La pelicula no esta en el catalogo");
            } else {
                respuesta.setMessage("Pelicula encontrada");
                respuesta.setPelicula(list);
                respuesta.setNombre(datos.getNombre());
                respuesta.setNoTC(datos.getNoTC());
                respuesta.setTiempo(datos.getTiempo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMessage("Error en BD al consultar nombre: "
                    + datos.getPelicula().getNombre());
        }
        return respuesta;
    }

    public RespuestaDatosTiempo validacionTiempo(RespuestaValidacionPelicula datos) {
        RespuestaDatosTiempo respuesta = new RespuestaDatosTiempo();
        try {
            List<Pelicula> list = peliculasDao.qryTiempoCobrar(datos);
            if (datos.getTiempo() > list.get(0).getDuracion()) {
                respuesta.setMessage("Tiempo mayor a duracion de la pelicula solo se cobrara "
                        + list.get(0).getDuracion() + " segundos");
                respuesta.setPelicula(list);
                respuesta.setNombre(datos.getNombre());
                respuesta.setTiempo(list.get(0).getDuracion());
                respuesta.setNoTC(datos.getNoTC());
                respuesta.setMayor(true);
            } else {
                respuesta.setMessage("OK");
                respuesta.setPelicula(list);
                respuesta.setNombre(datos.getNombre());
                respuesta.setTiempo(datos.getTiempo());
                respuesta.setNoTC(datos.getNoTC());
                respuesta.setMayor(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMessage("Error en BD al consultar la duracion de la pelicula");
        }

        return respuesta;
    }

    public RespuestaCobroTotal validacionSaldo(RespuestaDatosTiempo datos) {
        RespuestaCobroTotal respuesta = new RespuestaCobroTotal();
        try {
            List<Pelicula> jsonPelicula = datos.getPelicula();
            List<Tarjeta> jsonTC = peliculasDao.qryCobroTotal(datos);

            BigDecimal cobro;

            /* Si el timepo a rentar es mayor a la duracion */
            if (datos.isMayor()) {
                cobro = jsonPelicula.get(0).getPrecio().multiply(BigDecimal.valueOf(jsonPelicula.get(0).getDuracion()));
                int comparacion = cobro.compareTo(jsonTC.get(0).getSaldoDisponible());
                if (comparacion <= 0) {
                    respuesta.setMessage("Se cobrara " + cobro + " pesos por " + datos.getTiempo() + " segundos");
                    respuesta.setPelicula(jsonPelicula);
                    respuesta.setTarjetas(jsonTC);
                    respuesta.setTiempo(jsonPelicula.get(0).getDuracion());
                    respuesta.setCobro(cobro);
                    respuesta.setDisponibilidad(true);
                } else if (comparacion > 0) {
                    respuesta.setMessage("No se puede realizar el cobro, a cobrar: "
                            + cobro + ", saldo: " + jsonTC.get(0).getSaldoDisponible());
                    respuesta.setPelicula(jsonPelicula);
                    respuesta.setTarjetas(jsonTC);
                    respuesta.setTiempo(jsonPelicula.get(0).getDuracion());
                    respuesta.setCobro(cobro);
                    respuesta.setDisponibilidad(false);
                }
            } else { /* Si el timepo a rentar no es mayor a la duracion */
                cobro = jsonPelicula.get(0).getPrecio().multiply(BigDecimal.valueOf(datos.getTiempo()));
                int comparacion = cobro.compareTo(jsonTC.get(0).getSaldoDisponible());
                if (comparacion <= 0) {
                    respuesta.setMessage("Se cobrara " + cobro + " pesos por " + datos.getTiempo() + " segundos");
                    respuesta.setPelicula(jsonPelicula);
                    respuesta.setTarjetas(jsonTC);
                    respuesta.setTiempo(datos.getTiempo());
                    respuesta.setCobro(cobro);
                    respuesta.setDisponibilidad(true);
                } else if (comparacion > 0) {
                    respuesta.setMessage("No se puede realizar el cobro, a cobrar: "
                            + cobro + ", saldo: " + jsonTC.get(0).getSaldoDisponible());
                    respuesta.setPelicula(jsonPelicula);
                    respuesta.setTarjetas(jsonTC);
                    respuesta.setTiempo(datos.getTiempo());
                    respuesta.setCobro(cobro);
                    respuesta.setDisponibilidad(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMessage("Error en BD al consultar el saldo de la tarjeta");
        }
        return respuesta;
    }

    public RespuestaStreamInit realizarCobro(RespuestaCobroTotal datos) {
        RespuestaStreamInit respuesta = new RespuestaStreamInit();
        try {
            if (datos.isDisponibilidad()) {
                peliculasDao.cobrar(datos.getCobro(), datos.getTarjetas());
                respuesta.setMessage("Se realizo el cobro total de la pelicula de: " + datos.getCobro());
                respuesta.setPagoRealizado(true);
                respuesta.setTiempo(datos.getTiempo());
                respuesta.setPelicula(datos.getPelicula());
                respuesta.setTarjetas(datos.getTarjetas());

            } else {
                respuesta.setMessage(
                        "No se pudo realizar el cobro total de la pelicula con un costo de: " + datos.getCobro());
                respuesta.setPagoRealizado(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMessage("Error en BD al realizar el cobro");
        }

        return respuesta;
    }

    public RespuestaStream validacionStream(RespuestaStreamInit datos) {
        RespuestaStream respuesta = new RespuestaStream();

        try {
            if (datos.isPagoRealizado()) {
                peliculasDao.renta(datos.getTarjetas().get(0).getNombre(), datos.getPelicula().get(0).getId(),
                        datos.getTiempo());
                respuesta.setMessage("Inicio de stream correcto, contrataste " + datos.getTiempo() + " segundos");
                respuesta.setPeliculas(datos.getPelicula());
                respuesta.setTiempo(datos.getTiempo());
                respuesta.setInicioStream(true);
            } else {
                respuesta.setMessage("Inicio de stream correcto, contrataste " + datos.getTiempo() + " segundos");
                respuesta.setInicioStream(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            respuesta.setMessage("Error en BD al realizar el cobro");
        }

        return respuesta;
    }

    /* Nombre de la cola de respuesta del microservicio */
    @Value("${queue.inicioStream.out}")
    private String outQueueCobro;

    @Autowired
    JmsSender sender;

    public RespuestaInicioStream inicioStream(RespuestaStream datos) {
        RespuestaInicioStream respuesta = new RespuestaInicioStream();
        LocalDateTime tiempoInicial = LocalDateTime.now();
        LocalDateTime tiempoFinal = tiempoInicial.plus(datos.getTiempo(), ChronoUnit.SECONDS);

        if (datos.isInicioStream()) {
            respuesta.setMessage("Iniciando stream para: " + datos.getPeliculas().get(0).getNombre());
            respuesta.setVisualizar(true);
            System.out.println(respuesta.getMessage());

            while (tiempoInicial.isBefore(tiempoFinal)) {
                respuesta.setMessage("Vizualizando la pelicula de " + datos.getPeliculas().get(0).getNombre());
                System.out.println(String.format("Mensaje streaming: %s",
                        respuesta.toString()));
                sender.sendMessage(respuesta.toString(), outQueueCobro);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Actualizar tiempoInicial
                tiempoInicial = tiempoInicial.plusSeconds(1);
            }

            if (tiempoInicial.isAfter(tiempoFinal)) {
                respuesta.setMessage("Se ha agotado el tiempo de visualización.");
            } else if (tiempoInicial.isEqual(tiempoFinal)) {
                respuesta.setMessage("Tiempo de visualización terminado.");
                respuesta.setVisualizar(false);
            }
        } else {
            respuesta.setMessage("No se pudo iniciar el stream.");
        }

        return respuesta;
    }
}
