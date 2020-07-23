
package Crunch.excepciones;

/**
 *
 * @author lauta
 */
public class ExcepcionServicio extends Exception{
    
    public ExcepcionServicio(){}
    
    public ExcepcionServicio(String mensaje){
        super(mensaje);
    }
}
