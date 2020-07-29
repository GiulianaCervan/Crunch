package Crunch.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Cliente extends Usuario {

    private String domicilio;

    @OneToMany
    private List<Puntos> puntos;

    @OneToMany
    private List<Cupon> cupones;

    @OneToMany
    private List<Valoracion> valoraciones;

    public Cliente() {
        super();
    }

    public Cliente(String domicilio, List<Puntos> puntos, List<Cupon> cuponPromo, List<Valoracion> valoraciones, String mail, String clave, String nombre, String apellido, String telefono) {
        super(mail, clave, nombre, apellido, telefono);
        this.domicilio = domicilio;
        this.puntos = puntos;
        this.cupones = cuponPromo;
        this.valoraciones = valoraciones;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public List<Cupon> getCupones() {
        return cupones;
    }

    public void setCupones(List<Cupon> cupones) {
        this.cupones = cupones;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public List<Puntos> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<Puntos> puntos) {
        this.puntos = puntos;
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
