/**
 * 
 */
package com.soa.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.soa.dto.Pelicula;
import com.soa.dto.RespuestaDatosTiempo;
import com.soa.dto.RespuestaValidacionPelicula;
import com.soa.dto.Tarjeta;

/**
 * Capa de acceso a datos.
 */
@Repository
public class PeliculasDao {

    /**
     * Objeto especializado en acceso a la BD.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Consulta de validacion de la pelicula a rentar
     * 
     * @param pelicula
     * @return
     */
    public List<Pelicula> qryPeliculas(Pelicula pelicula) {
        List<Pelicula> resp = new ArrayList<>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select id_pelicula, nombre, precio, duracion from peliculas where nombre = '" + pelicula.getNombre()
                        + "'");
        // System.out.println(list.getClass().getName());

        for (Map<String, Object> registro : list) {
            Pelicula peli = new Pelicula();
            peli.setId((Integer) registro.get("id_pelicula"));
            peli.setNombre((String) registro.get("nombre"));
            peli.setPrecio((BigDecimal) registro.get("precio"));
            peli.setDuracion((Integer) registro.get("duracion"));
            resp.add(peli);
        }
        return resp;
    }

    public List<Pelicula> qryTiempo(Pelicula pelicula) {
        List<Pelicula> resp = new ArrayList<>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select duracion from peliculas where nombre = '" + pelicula.getNombre() + "'");
        // System.out.println(list.getClass().getName());

        for (Map<String, Object> registro : list) {
            Pelicula peli = new Pelicula();
            peli.setPrecio((BigDecimal) registro.get("precio"));

            resp.add(peli);
        }
        return resp;
    }

    public List<Pelicula> qryTiempoCobrar(RespuestaValidacionPelicula datos) {
        // Sacar tiempo quiere, nombre de la pelicula, duracion de la pelicula,
        List<Pelicula> peliculas = new ArrayList<>();

        List<Map<String, Object>> tiempoBD = jdbcTemplate.queryForList(
                "select id_pelicula,nombre,duracion,precio from peliculas where nombre = '"
                        + datos.getPelicula().get(0).getNombre()
                        + "'");

        for (Map<String, Object> registro : tiempoBD) {
            Pelicula peli = new Pelicula();
            peli.setId((Integer) registro.get("id_pelicula"));
            peli.setNombre((String) registro.get("nombre"));
            peli.setDuracion((Integer) registro.get("duracion"));
            peli.setPrecio((BigDecimal) registro.get("precio"));
            peliculas.add(peli);
        }
        return peliculas;
    }

    public List<Tarjeta> qryCobroTotal(RespuestaDatosTiempo datos) {
        // Sacar tiempo quiere, nombre de la pelicula, duracion de la pelicula,
        List<Tarjeta> tarjetas = new ArrayList<>();

        List<Map<String, Object>> datosTC = jdbcTemplate.queryForList(
                "select id_tarjeta,nombre,numero, saldo from tarjeta_cd where numero = '" + datos.getNoTC() + "'");

        for (Map<String, Object> registro : datosTC) {
            Tarjeta Tc = new Tarjeta();
            Tc.setId((Integer) registro.get("id_tarjeta"));
            Tc.setNombre((String) registro.get("nombre"));
            Tc.setNumero((String) registro.get("numero"));
            Tc.setSaldoDisponible((BigDecimal) registro.get("saldo"));
            tarjetas.add(Tc);
        }
        return tarjetas;
    }

    public void cobrar(BigDecimal cobro, List<Tarjeta> tarjeta) throws Exception {

        jdbcTemplate.execute(
                "UPDATE tarjeta_cd SET saldo = saldo -" + cobro + " WHERE numero =" + tarjeta.get(0).getNumero());
    }

    public void renta(String nombre, int id, Integer tiempo) {
        jdbcTemplate
                .execute("INSERT INTO clientes (nombre, pelicula_rentada, tiempo_renta)" + "VALUES ( '" + nombre + "',"
                        + id + "," + tiempo + ")");

    }
}
