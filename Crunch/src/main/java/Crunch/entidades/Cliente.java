
package Crunch.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lauta
 */
@Entity
public class Cliente extends Usuario{
    
    private String domicilio;
    private Integer puntos;
    @OneToMany
    private List<Cupon> cuponPromo;
    @OneToMany
    private List<CuponDeCanje> cuponCanje;
    @OneToMany
    private List<Raspadita> raspaditas;
    @OneToMany
    private List<Valoracion> valoraciones;

    public Cliente() {
        super();
    }

    public Cliente(String domicilio, Integer puntos, List<Cupon> cuponPromo, List<CuponDeCanje> cuponCanje, List<Raspadita> raspaditas, List<Valoracion> valoraciones, String mail, String clave, String nombre, String apellido, String telefono) {
        super(mail, clave, nombre, apellido, telefono);
        this.domicilio = domicilio;
        this.puntos = puntos;
        this.cuponPromo = cuponPromo;
        this.cuponCanje = cuponCanje;
        this.raspaditas = raspaditas;
        this.valoraciones = valoraciones;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
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


    

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String getClave() {
        return clave;
    }

    @Override
    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getApellido() {
        return apellido;
    }

    @Override
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String getTelefono() {
        return telefono;
    }

    @Override
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

   


}
