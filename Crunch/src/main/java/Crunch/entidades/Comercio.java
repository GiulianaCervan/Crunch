
package Crunch.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Comercio {
    
    @Id
    private String mail;
    private String clave;
    private String nombreComercio;
    private String nombreDuenio;
    private String apellidoDuenio;
    private String telefono;
    private String direccion;
    @OneToMany
    private List<Rubro> rubros;
    @OneToMany
    private List<Cupon> cuponesPromo;
    @OneToMany
    private List<CuponDeCanje> cuponesCanje;
    @OneToMany
    private List<Raspadita> raspaditas;
    private Float reputacion;
    @OneToMany
    private List<Valoracion> valoraciones;

    public Comercio() {
    }

    public Comercio(String mail, String clave, String nombreComercio, String nombreDuenio, String apellidoDuenio, String telefono, String direccion, List<Rubro> rubros, List<Cupon> cuponesPromo, List<CuponDeCanje> cuponesCanje, List<Raspadita> raspaditas, Float reputacion, List<Valoracion> valoraciones) {
        this.mail = mail;
        this.clave = clave;
        this.nombreComercio = nombreComercio;
        this.nombreDuenio = nombreDuenio;
        this.apellidoDuenio = apellidoDuenio;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rubros = rubros;
        this.cuponesPromo = cuponesPromo;
        this.cuponesCanje = cuponesCanje;
        this.raspaditas = raspaditas;
        this.reputacion = reputacion;
        this.valoraciones = valoraciones;
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

    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombreComercio) {
        this.nombreComercio = nombreComercio;
    }

    public String getNombreDuenio() {
        return nombreDuenio;
    }

    public void setNombreDuenio(String nombreDuenio) {
        this.nombreDuenio = nombreDuenio;
    }

    public String getApellidoDuenio() {
        return apellidoDuenio;
    }

    public void setApellidoDuenio(String apellidoDuenio) {
        this.apellidoDuenio = apellidoDuenio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Rubro> getRubros() {
        return rubros;
    }

    public void setRubros(List<Rubro> rubros) {
        this.rubros = rubros;
    }

    public List<Cupon> getCuponesPromo() {
        return cuponesPromo;
    }

    public void setCuponesPromo(List<Cupon> cuponesPromo) {
        this.cuponesPromo = cuponesPromo;
    }

    public List<CuponDeCanje> getCuponesCanje() {
        return cuponesCanje;
    }

    public void setCuponesCanje(List<CuponDeCanje> cuponesCanje) {
        this.cuponesCanje = cuponesCanje;
    }

    public List<Raspadita> getRaspaditas() {
        return raspaditas;
    }

    public void setRaspaditas(List<Raspadita> raspaditas) {
        this.raspaditas = raspaditas;
    }

    public Float getReputacion() {
        return reputacion;
    }

    public void setReputacion(Float reputacion) {
        this.reputacion = reputacion;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }
    
}
