/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
