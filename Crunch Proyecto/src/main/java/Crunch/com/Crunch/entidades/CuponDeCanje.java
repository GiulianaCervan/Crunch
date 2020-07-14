
package Crunch.com.Crunch.entidades;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;

@Entity
public class CuponDeCanje extends Cupon implements Serializable{
    
    private Integer costo;

    public CuponDeCanje() {
        super();
    }

    public CuponDeCanje(Integer costo, String id, String titulo, String descripcion, Calendar vencimiento) {
        super(id, titulo, descripcion, vencimiento);
        this.costo = costo;
    }

    public Integer getCosto() {
        return costo;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public Calendar getVencimiento() {
        return vencimiento;
    }

    @Override
    public void setVencimiento(Calendar vencimiento) {
        this.vencimiento = vencimiento;
    }
    
    
}
