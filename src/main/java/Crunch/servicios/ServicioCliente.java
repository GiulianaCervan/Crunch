package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Puntos;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicioCliente {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ComercioRepositorio comercioRepositorio;
    @Autowired
    private ServicioComercio servicioComercio;

    /**
     * Se le ingresa el mail de cliente registrado y devuelve el Objeto Cliente
     *
     * @param mail
     * @return
     * @throws ExcepcionServicio
     */
    public Cliente buscarPorId(String mail) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El campo mail no puede estar vacio o ser nulo");
        }

        Optional<Cliente> respuesta = clienteRepositorio.findById(mail);
        Cliente cliente = null;
        if (respuesta.isPresent()) {
            cliente = respuesta.get();

        } else {
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
    public void crear(String mail, String clave, String clave2, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        validar(mail, clave, clave2, nombre, apellido, domicilio, telefono);

        List<Comercio> comerciosRegistrados = comercioRepositorio.findAll();

        for (Comercio comercio : comerciosRegistrados) {

            if (comercio.getMail().equals(mail)) {
                throw new ExcepcionServicio("Lo lamentamos, pero ya hay un comercio regitrado con ese mail, para usar la web como cliente use un mail particular");
            }
        }
        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
        /**
         * Saqué del método crear los atributos de: puntos,
         * cuponPromo,raspaditas,valoraciones. ATTE Lauta
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
    public void modificar(String mail, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        validarModificar(mail, nombre, apellido, domicilio, telefono);

        Optional<Cliente> respuesta = clienteRepositorio.findById(mail);
        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();

            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDomicilio(domicilio);
            cliente.setTelefono(telefono);

            clienteRepositorio.save(cliente);

        } else {
            throw new ExcepcionServicio("No se ha encontrado el cliente solicitado.");
        }
    }

    /**
     * Se le otorga un mail o pedazo de el y devuelve una lista de Clientes con
     * las coicidencias parciales
     *
     * @param mail
     * @return
     */
    public List<Cliente> buscarClientes(String mail) {

        List<Cliente> clientes = clienteRepositorio.buscarClientesPorMail("%" + mail + "%");

        return clientes;
    }

    /**
     * Este método otorga los puntos que tiene el cliente dependiendo del
     * comercio
     *
     * @param cliente
     * @param comercio
     * @return
     */
    public Integer puntosPorComercio(String mailCliente, String mailComercio) throws ExcepcionServicio {

        Cliente cliente = buscarPorId(mailCliente);
        Comercio comercio = servicioComercio.buscarPorId(mailComercio);

        for (Puntos punto : cliente.getPuntos()) {

            if (punto.getComercio().equals(comercio)) {
                return punto.getCantidad();
            }

        }
        return 0;
    }

    public List<Puntos> mostrarPuntos(String mail) {

        Cliente cliente = clienteRepositorio.getOne(mail);

        return cliente.getPuntos();
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
    private void validar(String mail, String clave, String clave2, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ExcepcionServicio("La clave no puede ser nula o estar vacia y tiene que ser mayor a 6 caracteres.");
        }

        if (!clave.equals(clave2)) {
            throw new ExcepcionServicio("Las claves deben ser iguales");
        }

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

    private void validarModificar(String mail, String nombre, String apellido, String domicilio, String telefono) throws ExcepcionServicio {
        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
        }

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

}
