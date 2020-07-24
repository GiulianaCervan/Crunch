package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
import Crunch.entidades.Foto;
import Crunch.entidades.Raspadita;
import Crunch.entidades.RubroAsignado;
import Crunch.entidades.Valoracion;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ComercioRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioComercio {

    @Autowired
    private ComercioRepositorio comercioRepositorio;
    @Autowired
    private ServicioFoto servicioFoto;

    /**
     * Se le ingresa el mail del comercio y devuelve el Objeto Comercio
     * 
     * @param mail
     * @return
     * @throws ExcepcionServicio 
     */
    public Comercio buscarPorId(String mail) throws ExcepcionServicio{
        
        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El campo mail no puede estar vacio o ser nulo");
        }
        
        Optional<Comercio> respuesta = comercioRepositorio.findById(mail);
        Comercio comercio = null;
        if (respuesta.isPresent()) {
            comercio = respuesta.get();
            
        }else{
            throw new ExcepcionServicio("El comercio no fue encontrado con el mail otorgado");
        }
        return comercio;
        
        
    }
    /**
     * Este método crea y guarda en la base de datos un objeto Cliente.
     *
     * @param nombreComercio
     * @param direccion
     * @param rubros
     * @param mail
     * @param clave
     * @param nombre
     * @param apellido
     * @param telefono
     * @param clave2
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(MultipartFile archivo,String mail, String clave, String clave2, String nombre, String apellido, String telefono, String direccion, String nombreComercio, String rubro) throws ExcepcionServicio {
        
        validar(mail, clave, clave2, nombreComercio, nombre, apellido, telefono, direccion, rubro);

        
        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);

        Comercio comercio = new Comercio();
        /**
         * Saqué del método crear los atributos de:
         * cuponesPromo,cuponesCanje,raspaditas,reputacion,valoracion
         *                          ATTE Lauta
         */
        comercio.setMail(mail);
        comercio.setClave(claveEncriptada);
        comercio.setNombre(nombre);
        comercio.setApellido(apellido);
        comercio.setTelefono(telefono);
        comercio.setDireccion(direccion);
        comercio.setNombreComercio(nombreComercio);
//        comercio.setRubros();

        Foto foto = servicioFoto.guardar(archivo);
        comercio.setFoto(foto);
        
        comercioRepositorio.save(comercio);
    }

    /**
     * Este método encuentra y modifica un objeto Comercio
     *
     * @param mail
     * @param clave
     * @param clave2
     * @param nombreComercio
     * @param nombre
     * @param apellido
     * @param telefono
     * @param direccion
     * @param rubros
     * @throws ExcepcionServicio
     */
    @Transactional
    public void modificar(String mail, String clave, String clave2, String nombreComercio, String nombre, String apellido, String telefono, String direccion, String rubros) throws ExcepcionServicio {
        validar(mail, clave, clave2, nombreComercio, nombre, apellido, telefono, direccion, rubros);

        Optional<Comercio> respuesta = comercioRepositorio.findById(mail);
        if (respuesta.isPresent()) {

            Comercio comercio = respuesta.get();

            String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
            comercio.setClave(claveEncriptada);
            comercio.setNombreComercio(nombreComercio);
            comercio.setNombre(nombre);
            comercio.setApellido(apellido);
            comercio.setTelefono(telefono);
            comercio.setDireccion(direccion);
//            comercio.setRubros(rubros);

            comercioRepositorio.save(comercio);
        } else {
            throw new ExcepcionServicio("No se ha encontrado el comercio solicitado.");
        }
    }

    /**
     * Este método lo utilizo para poder validar el comercio que quiero
     * registrar. Todavía está incompleto, quizá podemos agregarle más adelante
     * una segunda clave a los métodos cuando se llene el formulario de front y
     * confirmar que la clave se repitió
     *
     * @param mail
     * @param clave
     * @param clave2
     * @param nombreComercio
     * @param nombre
     * @param apellido
     * @param telefono
     * @param direccion
     * @param rubros
     * @throws ExcepcionServicio
     */
    private void validar(String mail, String clave, String clave2, String nombreComercio, String nombre, String apellido, String telefono, String direccion, String rubros) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ExcepcionServicio("La clave no puede ser nula o estar vacia y tiene que ser mayor a 6 caracteres.");
        }

        if (!clave.equals(clave2)) {
            throw new ExcepcionServicio("Las claves deben ser iguales");
        }

        if (nombreComercio == null || nombreComercio.isEmpty()) {
            throw new ExcepcionServicio("El nombre del comercio no puede ser nulo o estar vacío.");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ExcepcionServicio("El nombre del dueño no puede ser nulo o estar vacío.");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ExcepcionServicio("El apellido del dueño no puede ser nulo o estar vacío.");
        }

        if (direccion == null || direccion.isEmpty()) {
            throw new ExcepcionServicio("La dirección no puede ser nulo o estar vacío.");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new ExcepcionServicio("El telefono no puede ser nulo o estar vacío.");
        }

//        if (rubros == null || rubros.isEmpty()) {
//            throw new ExcepcionServicio("El/los rubro/s no puede/n ser nulo/s");
//        }

    }

}
