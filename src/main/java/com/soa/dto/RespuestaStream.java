/**
 * 
 */
package com.soa.dto;

import java.util.List;

import com.google.gson.Gson;

/**
 * 
 */
public class RespuestaStream {

    private String message;
    
    private List<Pelicula> peliculas;
    private List<Tarjeta> tarjetas;

    private Integer tiempo;
    private boolean inicioStream;

    /**
     * @return the Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param Message the Message to set
     */
    public void setMessage(String Message) {
        this.message = Message;
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
     * @return the incioStream
     */
    public boolean isInicioStream() {
        return inicioStream;
    }

    /**
     * @param incioStream the incioStream to set
     */
    public void setInicioStream(boolean incioStream) {
        this.inicioStream = incioStream;
    }

    
    /**
     * @return the peliculas
     */
    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    /**
     * @param peliculas the peliculas to set
     */
    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
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

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

}
