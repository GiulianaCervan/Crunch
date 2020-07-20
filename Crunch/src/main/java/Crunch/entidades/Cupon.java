
package Crunch.entidades;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Cupon {
       
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    protected String titulo;
    protected String descripcion;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Calendar vencimiento;
    protected boolean disponible;

    protected boolean vencido;
    
    @ManyToOne
    protected Comercio comercio;
    @ManyToOne
    protected Cliente cliente;
    
    public Cupon() {
    }

    public Cupon(String id, String titulo, String descripcion, Calendar vencimiento, boolean disponible, boolean vencido, Comercio comercio, Cliente cliente) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
        this.disponible = disponible;
        this.vencido = vencido;
        this.comercio = comercio;
        this.cliente = cliente;
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

    public Calendar getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Calendar vencimiento) {
        this.vencimiento = vencimiento;
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
    
    
    
    
    
}
