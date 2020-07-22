package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicioCliente{

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    /**
     * Se le ingresa el mail de cliente registrado y devuelve el Objeto Cliente
     * 
     * @param mail
     * @return
     * @throws ExcepcionServicio 
     */
    public Cliente buscarPorId(String mail) throws ExcepcionServicio{
        
        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El campo mail no puede estar vacio o ser nulo");
        }
        
        Optional<Cliente> respuesta = clienteRepositorio.findById(mail);
        Cliente cliente = null;
        if (respuesta.isPresent()) {
            cliente = respuesta.get();
            
        }else{
            throw new ExcepcionServicio("El cliente no fue encontrado con el mail otorgado");
        }
        return cliente;
        
        
    }
    /**
     * Este método es el que usaríamos para crear usuarios y guardarlos en
     * nuestra base de datos.
     *
     * @param mail
     * @param clave
     * @param clave2
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(String mail, String clave,String clave2, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        validar(mail, clave,clave2, nombre, apellido, domicilio, telefono);

        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
        /**
         * Saqué del método crear los atributos de:
         * puntos, cuponPromo,raspaditas,valoraciones.
         *                          ATTE Lauta
         */
        Cliente cliente = new Cliente();
        cliente.setMail(mail);
        cliente.setClave(claveEncriptada);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setDomicilio(domicilio);
        
        
        clienteRepositorio.save(cliente);
    }

    /**
     * Este método encuentra y modifica un objeto Cliente 
     * @param mail
     * @param clave
     * @param clave2
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @throws ExcepcionServicio 
     */
    @Transactional
    public void modificar(String mail, String clave,String clave2, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        validar(mail, clave,clave2, nombre, apellido, domicilio, telefono);

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
     * @param clave2
     * @param nombre
     * @param apellido
     * @param domicilio
     * @param telefono
     * @throws ExcepcionServicio
     */
    private void validar(String mail, String clave,String clave2 ,String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ExcepcionServicio("La clave no puede ser nula o estar vacia y tiene que ser mayor a 6 caracteres.");
        }

        if(!clave.equals(clave2)){
            throw new ExcepcionServicio("Las claves deben ser iguales");
        }
        
        if (nombre == null || nombre.isEmpty()) {
            throw new ExcepcionServicio("El nombre no puede ser nulo o estar vacío.");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ExcepcionServicio("El apellido no puede ser nulo o estar vacío.");
        }

//        if (domicilio == null || domicilio.isEmpty()) {
//            throw new ExcepcionServicio("El domicilio no puede ser nulo o estar vacío.");
//        }

        if (telefono == null || telefono.isEmpty()) {
            throw new ExcepcionServicio("El telefono no puede ser nulo o estar vacío.");
        }

    }

}
