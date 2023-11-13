/**
 * 
 */
package com.soa.dto;

import com.google.gson.Gson;

/**
 * 
 */
public class JsonInit {
    
    private Pelicula pelicula;
    private Integer tiempo;
    private String nombre;
    private String noTC;

    
    /**
     * @return the pelicula
     */
    public Pelicula getPelicula() {
        return pelicula;
    }
    /**
     * @param pelicula the pelicula to set
     */
    public void setPelicula(Pelicula pelicula) {
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
    
    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    
    

}
