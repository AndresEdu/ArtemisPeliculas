package com.soa.dto;

import java.math.BigDecimal;

import com.google.gson.Gson;

public class Tarjeta {

    private Integer id;
    private String nombre;
    private String numero;
    private BigDecimal saldoDisponible;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the saldoDisponible
     */
    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    /**
     * @param saldoDisponible the saldoDisponible to set
     */
    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }


}
