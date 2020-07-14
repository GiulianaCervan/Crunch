
package Crunch.com.Crunch.entidades;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Cupon implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    protected String titulo;
    protected String descripcion;
    protected Calendar vencimiento;

    public Cupon() {
    }

    public Cupon(String id, String titulo, String descripcion, Calendar vencimiento) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.vencimiento = vencimiento;
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
    
    
}
