
package Crunch.com.Crunch.Entidades;


public class Valoracion {
    private String identificador;
    private Integer puntuacion;

    public Valoracion() {
    }
    

    public Valoracion(String identificador, Integer puntuacion) {
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
    

}
