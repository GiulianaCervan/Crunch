/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author lauta
 */
@Entity
public abstract class Usuario {

    @Id
    protected String mail;
    protected String clave;
    protected String nombre;
    protected String apellido;
    protected String telefono;

    public Usuario() {
    }

    public Usuario(String mail, String clave, String nombre, String apellido, String telefono) {
        this.mail = mail;
        this.clave = clave;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
