
package Crunch.com.Crunch.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Cliente {
    
    @Id
    private String mail;
    
    private String clave;
    private String nombre;
    private String apellido;
    private String domicilio;
    private String telefono;
    private Integer puntos;
    private List<Cupon> cuponPromo;
    private List<CuponDeCanje> cuponCanje;
    private List<Raspadita> raspaditas;
    private List<Valoracion> valoraciones;
    
}
