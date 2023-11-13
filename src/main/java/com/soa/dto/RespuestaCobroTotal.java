package com.soa.dto;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.Gson;

/**
 * Clase que modela la informacion resumida de una persona.
 */
public class RespuestaCobroTotal {

    private String message;
    private List<Pelicula> pelicula;
    private List<Tarjeta> tarjetas;
    private Integer tiempo;
    private String nombre;
    private String noTC;
    private boolean disponibilidad;
    private BigDecimal cobro;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the pelicula
     */
    public List<Pelicula> getPelicula() {
        return pelicula;
    }

    /**
     * @param pelicula the pelicula to set
     */
    public void setPelicula(List<Pelicula> pelicula) {
        this.pelicula = pelicula;
    }

    /**
     * @return the tarjetas
     */
    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    /**
     * @param tarjetas the tarjetas to set
     */
    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    /**
     * @return the tiempo
     */
    public Integer getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo the tiempo to set
     */
    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the noTC
     */
    public String getNoTC() {
        return noTC;
    }

    /**
     * @param noTC the noTC to set
     */
    public void setNoTC(String noTC) {
        this.noTC = noTC;
    }

    /**
     * @return the disponibilidad
     */
    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    /**
     * @param disponibilidad the disponibilidad to set
     */
    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    /**
     * @return the cobro
     */
    public BigDecimal getCobro() {
        return cobro;
    }

    /**
     * @param cobro the cobro to set
     */
    public void setCobro(BigDecimal cobro) {
        this.cobro = cobro;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

}
