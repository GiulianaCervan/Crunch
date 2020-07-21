package Crunch.entidades;

import Crunch.utilidades.Rubro;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class Comercio extends Usuario {

    private String nombreComercio;
    private String direccion;

    @OneToMany
    private List<RubroAsignado> rubros;

    @OneToMany
    private List<Cupon> cuponesPromo;

    @OneToMany
    private List<Raspadita> raspaditas;

    private Float reputacion;

    @OneToMany
    private List<Valoracion> valoraciones;

    @OneToOne
    private Foto foto;

    public Comercio() {
        super();
    }

    public Comercio(String nombreComercio, String direccion, List<RubroAsignado> rubros, List<Cupon> cuponesPromo, List<Raspadita> raspaditas, Float reputacion, List<Valoracion> valoraciones, String mail, String clave, String nombre, String apellido, String telefono) {
        super(mail, clave, nombre, apellido, telefono);
        this.nombreComercio = nombreComercio;
        this.direccion = direccion;
        this.rubros = rubros;
        this.cuponesPromo = cuponesPromo;
        this.raspaditas = raspaditas;
        this.reputacion = reputacion;
        this.valoraciones = valoraciones;
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

    public List<Cupon> getCuponesPromo() {
        return cuponesPromo;
    }

    public void setCuponesPromo(List<Cupon> cuponesPromo) {
        this.cuponesPromo = cuponesPromo;
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

    public Foto getFoto() {

        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
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
