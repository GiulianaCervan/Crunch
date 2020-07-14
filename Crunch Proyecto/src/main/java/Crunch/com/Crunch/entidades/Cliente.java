
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

    public Cliente() {
    }

    
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public List<Cupon> getCuponPromo() {
        return cuponPromo;
    }

    public void setCuponPromo(List<Cupon> cuponPromo) {
        this.cuponPromo = cuponPromo;
    }

    public List<CuponDeCanje> getCuponCanje() {
        return cuponCanje;
    }

    public void setCuponCanje(List<CuponDeCanje> cuponCanje) {
        this.cuponCanje = cuponCanje;
    }

    public List<Raspadita> getRaspaditas() {
        return raspaditas;
    }

    public void setRaspaditas(List<Raspadita> raspaditas) {
        this.raspaditas = raspaditas;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }
    
    
}
