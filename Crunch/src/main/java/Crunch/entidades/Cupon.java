
package Crunch.entidades;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    protected String mailComercio;

    public Cupon() {
    }

    public Cupon(String id, String titulo, String descripcion, Calendar vencimiento, boolean disponible, String mailComercio) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
        this.disponible = disponible;
        this.mailComercio = mailComercio;
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

    public String getMailComercio() {
        return mailComercio;
    }

    public void setMailComercio(String mailComercio) {
        this.mailComercio = mailComercio;
    }
    
    
    
}
