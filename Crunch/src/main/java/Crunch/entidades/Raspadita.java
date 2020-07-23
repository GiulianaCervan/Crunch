
package Crunch.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Raspadita {
    
    @Id
    private String identificador;
    private Integer puntuacion;

    public Raspadita() {
        
    }

    public Raspadita(String identificador, Integer puntuacion) {
        this.identificador = identificador;
        this.puntuacion = puntuacion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void canje() {

    }
}
