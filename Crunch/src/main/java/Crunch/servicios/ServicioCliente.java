/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Cupon;
import Crunch.entidades.CuponDeCanje;
import Crunch.entidades.Raspadita;
import Crunch.entidades.Valoracion;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
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
public class ServicioCliente implements UserDetailsService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    /**
     * Este método es el que usaríamos para crear usuarios y guardarlos en
     * nuestra base de datos.
     *
     * @param mail
     * @param clave
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @param puntos
     * @param cuponpromo
     * @param cuponCanje
     * @param raspaditas
     * @param valoraciones
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(String mail, String clave, String nombre, String apellido, String domicilio, String telefono, Integer puntos, List<Cupon> cuponpromo, List<CuponDeCanje> cuponCanje, List<Raspadita> raspaditas, List<Valoracion> valoraciones) throws ExcepcionServicio {

        validar(mail, clave, nombre, apellido, domicilio, telefono);

        Cliente cliente = new Cliente();
        cliente.setMail(mail);

        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
        cliente.setClave(claveEncriptada);

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDomicilio(domicilio);
        cliente.setTelefono(telefono);
        cliente.setPuntos(puntos);
        cliente.setCuponPromo(cuponpromo);
        cliente.setCuponCanje(cuponCanje);
        cliente.setRaspaditas(raspaditas);
        cliente.setValoraciones(valoraciones);

        clienteRepositorio.save(cliente);
    }

    /**
     * Este método encuentra y modifica un objeto Cliente 
     * @param mail
     * @param clave
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @throws ExcepcionServicio 
     */
    @Transactional
    public void modificar(String mail, String clave, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        validar(mail, clave, nombre, apellido, domicilio, telefono);

        Optional<Cliente> respuesta = clienteRepositorio.findById(mail);
        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            
            String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
            cliente.setClave(claveEncriptada);

            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDomicilio(domicilio);
            cliente.setTelefono(telefono);
            
        }else{
            throw new ExcepcionServicio("No se ha encontrado el cliente solicitado.");
        }
    }

    /**
     * Este método lo utilizo para poder validar el cliente que quiero
     * registrar. Todavía está incompleto, quizá podemos agregarle más adelante
     * una segunda clave a los métodos cuando se llene el formulario de front y
     * confirmar que la clave se repitió
     *
     * @param mail
     * @param clave
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @throws ExcepcionServicio
     */
    private void validar(String mail, String clave, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ExcepcionServicio("La clave no puede ser nula o estar vacia y tiene que ser mayor a 6 caracteres.");
        }

//        if(!clave.equals(clave2)){
//            throw new ExcepcionServicio("Las claves deben ser iguales");
//        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ExcepcionServicio("El nombre no puede ser nulo o estar vacío.");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ExcepcionServicio("El apellido no puede ser nulo o estar vacío.");
        }

        if (domicilio == null || domicilio.isEmpty()) {
            throw new ExcepcionServicio("El domicilio no puede ser nulo o estar vacío.");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new ExcepcionServicio("El telefono no puede ser nulo o estar vacío.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
