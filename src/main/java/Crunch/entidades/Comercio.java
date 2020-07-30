package Crunch.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Comercio extends Usuario {

    private String nombreComercio;
    private String direccion;

    @OneToMany
    private List<RubroAsignado> rubros;

    @OneToMany
    private List<Cupon> cuponesPromo;

    private Float reputacion;

    @OneToMany
    private List<Valoracion> valoraciones;

    @OneToOne
    private Foto foto;

    public Comercio() {
        super();
    }

    public Comercio(String nombreComercio, String direccion, List<RubroAsignado> rubros, List<Cupon> cuponesPromo, Float reputacion, List<Valoracion> valoraciones, Foto foto, String mail, String clave, String nombre, String apellido, String telefono) {
        super(mail, clave, nombre, apellido, telefono);
        this.nombreComercio = nombreComercio;
        this.direccion = direccion;
        this.rubros = rubros;
        this.cuponesPromo = cuponesPromo;
        this.reputacion = reputacion;
        this.valoraciones = valoraciones;
        this.foto = foto;
    }

    
    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombreComercio) {
        this.nombreComercio = nombreComercio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<RubroAsignado> getRubros() {
        return rubros;
    }

    public void setRubros(List<RubroAsignado> rubros) {
        this.rubros = rubros;
    }

    public List<Cupon> getCupones() {
        return cuponesPromo;
    }

    public void setCuponesPromo(List<Cupon> cuponesPromo) {
        this.cuponesPromo = cuponesPromo;
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

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

}
