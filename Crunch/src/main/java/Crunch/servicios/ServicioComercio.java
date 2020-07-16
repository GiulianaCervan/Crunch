/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
import Crunch.entidades.CuponDeCanje;
import Crunch.entidades.Raspadita;
import Crunch.entidades.Valoracion;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ComercioRepositorio;
import Crunch.utilidades.Rubro;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author lauta
 */
@Service
public class ServicioComercio implements UserDetailsService {

    @Autowired
    private ComercioRepositorio comercioRepositorio;
    /**
     * Este método crea y guarda en la base de datos un objeto Cliente.
     * @param mail
     * @param clave
     * @param nombreComercio
     * @param nombreDueño
     * @param apellidoDueño
     * @param telefono
     * @param direccion
     * @param rubros
     * @param cuponesPromo
     * @param cuponesCanje
     * @param raspaditas
     * @param reputacion
     * @param valoraciones
     * @throws ExcepcionServicio 
     */
    @Transactional
    public void crear(String mail, String clave, String nombreComercio, String nombreDueño, String apellidoDueño, String telefono, String direccion, List<Rubro> rubros, List<Cupon> cuponesPromo, List<CuponDeCanje> cuponesCanje, List<Raspadita> raspaditas, Float reputacion, List<Valoracion> valoraciones) throws ExcepcionServicio {

        validar(mail, clave, nombreComercio, nombreDueño, apellidoDueño, telefono, direccion, rubros);

        Comercio comercio = new Comercio(mail, clave, nombreComercio, nombreDueño, apellidoDueño, telefono, direccion, rubros, cuponesPromo, cuponesCanje, raspaditas, reputacion, valoraciones);

        comercioRepositorio.save(comercio);
    }

    /**
     * Este método encuentra y modifica un objeto Comercio
     * @param mail
     * @param clave
     * @param nombreComercio
     * @param nombreDueño
     * @param apellidoDueño
     * @param telefono
     * @param direccion
     * @param rubros
     * @throws ExcepcionServicio 
     */
    @Transactional
    public void modificar(String mail, String clave, String nombreComercio, String nombreDueño, String apellidoDueño, String telefono, String direccion, List<Rubro> rubros) throws ExcepcionServicio {
        validar(mail, clave, nombreComercio, nombreDueño, apellidoDueño, telefono, direccion, rubros);

        Optional<Comercio> respuesta = comercioRepositorio.findById(mail);
        if (respuesta.isPresent()) {

            Comercio comercio = respuesta.get();

            String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
            comercio.setClave(claveEncriptada);
            comercio.setNombreComercio(nombreComercio);
            comercio.setNombreDuenio(nombreDueño);
            comercio.setApellidoDuenio(apellidoDueño);
            comercio.setTelefono(telefono);
            comercio.setDireccion(direccion);
            comercio.setRubros(rubros);
            
            comercioRepositorio.save(comercio);
        }else{
            throw new ExcepcionServicio("No se ha encontrado el comercio solicitado.");
        }
    }

    /**
     * Este método lo utilizo para poder validar el comercio que quiero
     * registrar. Todavía está incompleto, quizá podemos agregarle más adelante
     * una segunda clave a los métodos cuando se llene el formulario de front y
     * confirmar que la clave se repitió 
     * @param mail
     * @param clave
     * @param nombreComercio
     * @param nombreDueño
     * @param apellidoDueño
     * @param telefono
     * @param direccion
     * @param rubros
     * @throws ExcepcionServicio 
     */
    private void validar(String mail, String clave, String nombreComercio, String nombreDueño, String apellidoDueño, String telefono, String direccion, List<Rubro> rubros) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ExcepcionServicio("La clave no puede ser nula o estar vacia y tiene que ser mayor a 6 caracteres.");
        }

//        if(!clave.equals(clave2)){
//            throw new ExcepcionServicio("Las claves deben ser iguales");
//        }
        if (nombreComercio == null || nombreComercio.isEmpty()) {
            throw new ExcepcionServicio("El nombre del comercio no puede ser nulo o estar vacío.");
        }
        if (nombreDueño == null || nombreDueño.isEmpty()) {
            throw new ExcepcionServicio("El nombre del dueño no puede ser nulo o estar vacío.");
        }
        if (apellidoDueño == null || apellidoDueño.isEmpty()) {
            throw new ExcepcionServicio("El apellido del dueño no puede ser nulo o estar vacío.");
        }

        if (direccion == null || direccion.isEmpty()) {
            throw new ExcepcionServicio("La dirección no puede ser nulo o estar vacío.");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new ExcepcionServicio("El telefono no puede ser nulo o estar vacío.");
        }

        if (rubros == null || rubros.isEmpty()) {
            throw new ExcepcionServicio("El/los rubro/s no puede/n ser nulo/s");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
