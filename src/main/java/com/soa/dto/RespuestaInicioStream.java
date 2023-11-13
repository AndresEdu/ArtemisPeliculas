/**
 * 
 */
package com.soa.dto;

import java.util.List;

import com.google.gson.Gson;

/**
 * 
 */
public class RespuestaInicioStream {
    
    private String message;
    private List<Pelicula> pelicula;
    private Integer tiempo;
    private boolean visualizar;
    
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
     * @return the visualizar
     */
    public boolean isVisualizar() {
        return visualizar;
    }
    /**
     * @param visualizar the visualizar to set
     */
    public void setVisualizar(boolean visualizar) {
        this.visualizar = visualizar;
    }
    
    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

}
