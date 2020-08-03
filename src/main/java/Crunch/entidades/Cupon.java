
package Crunch.entidades;

import Crunch.utilidades.TipoCupon;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Cupon {
       
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String titulo;
    private String descripcion;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date vencimiento;
    private boolean disponible = true;

    private boolean vencido = false;
    
    @Enumerated(EnumType.STRING)
    private TipoCupon tipo;
    
    @ManyToOne
    private Comercio comercio;
    
    @ManyToOne
    private Cliente cliente;
    
    private Integer costo;
    
    public Cupon() {
        
    }

    public Cupon(String id, String titulo, String descripcion, Date vencimiento, TipoCupon tipo, Comercio comercio, Cliente cliente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
        this.tipo = tipo;
        this.comercio = comercio;
        this.cliente = cliente;
    }

  

    public TipoCupon getTipo() {
        return tipo;
    }

    public void setTipo(TipoCupon tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }


    public boolean isVencido() {
        return vencido;
    }

    public void setVencido(boolean vencido) {
        this.vencido = vencido;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Integer getCosto() {
        return costo;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }
    
}
