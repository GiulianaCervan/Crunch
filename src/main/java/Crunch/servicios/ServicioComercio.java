package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Foto;
import Crunch.entidades.Puntos;
import Crunch.entidades.RubroAsignado;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import Crunch.repositorios.PuntosRepositorio;
import Crunch.repositorios.RubroRepositorio;
import Crunch.utilidades.Rubro;
import java.util.ArrayList;
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
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private PuntosRepositorio puntosRepositorio;
    @Autowired
    private ServicioCliente servicioCliente;

    @Autowired
    private RubroRepositorio rubroRepositorio;

    /**
     * Se le ingresa el mail del comercio y devuelve el Objeto Comercio
     *
     * @param mail
     * @return
     * @throws ExcepcionServicio
     */
    public Comercio buscarPorId(String mail) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El campo mail no puede estar vacio o ser nulo");
        }

        Optional<Comercio> respuesta = comercioRepositorio.findById(mail);
        Comercio comercio = null;
        if (respuesta.isPresent()) {
            comercio = respuesta.get();

        } else {
            throw new ExcepcionServicio("El comercio no fue encontrado con el mail otorgado");
        }
        return comercio;

    }

    /**
     * Este método crea y guarda en la base de datos un objeto Cliente.
     *
     * @param nombreComercio
     * @param direccion
     * @param rubro
     * @param mail
     * @param clave
     * @param nombre
     * @param apellido
     * @param telefono
     * @param clave2
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(MultipartFile archivo, String mail, String clave, String clave2, String nombre, String apellido, String telefono,
            String direccion, String nombreComercio, String rubro) throws ExcepcionServicio {

        validar(mail, clave, clave2, nombreComercio, nombre, apellido, telefono, direccion, rubro);

        List<Cliente> clienteRegistrados = clienteRepositorio.findAll();

        for (Cliente cliente : clienteRegistrados) {

            if (cliente.getMail().equals(mail)) {
                throw new ExcepcionServicio("Lo lamentamos, ya hay un cliente registrado con ese mail, para disfutrar de la web como comercio use un mail comercial");
            }
        }

        String claveEncriptada = new BCryptPasswordEncoder().encode(clave);

        Comercio comercio = new Comercio();

        comercio.setMail(mail);
        comercio.setClave(claveEncriptada);
        comercio.setNombre(nombre);
        comercio.setApellido(apellido);
        comercio.setTelefono(telefono);
        comercio.setDireccion(direccion);
        comercio.setNombreComercio(nombreComercio);

        String[] separados = rubro.split(",");

        List<RubroAsignado> rubros = new ArrayList<>();
        for (String separado : separados) {
            Rubro rubroEnum = Rubro.valueOf(separado);
            RubroAsignado rubroAsignado = new RubroAsignado();
            rubroAsignado.setRubro(rubroEnum);
            rubros.add(rubroAsignado);
            rubroRepositorio.save(rubroAsignado);
        }

        comercio.setRubros(rubros);

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
    public void modificar(String mail, String nombreComercio, String nombre, String apellido,
            String telefono, String direccion, String rubro) throws ExcepcionServicio {

        validarModificar(mail, nombreComercio, nombre, apellido, telefono, direccion, rubro);

        Optional<Comercio> respuesta = comercioRepositorio.findById(mail);

        if (respuesta.isPresent()) {

            Comercio comercio = respuesta.get();

            comercio.setNombreComercio(nombreComercio);
            comercio.setNombre(nombre);
            comercio.setApellido(apellido);
            comercio.setTelefono(telefono);
            comercio.setDireccion(direccion);

            try {
                if (rubro != null || !rubro.isEmpty()) {
                    String[] separados = rubro.split(",");
                    List<RubroAsignado> rubros = new ArrayList<>();

                    for (String separado : separados) {
                        Rubro rubroEnum = Rubro.valueOf(separado);
                        RubroAsignado rubroAsignado = new RubroAsignado();
                        rubroAsignado.setRubro(rubroEnum);
                        rubros.add(rubroAsignado);
                        rubroRepositorio.save(rubroAsignado);
                    }
                    comercio.setRubros(rubros);
                }
            } catch (Exception e) {
                comercioRepositorio.save(comercio);
            }

            comercioRepositorio.save(comercio);

        } else {
            throw new ExcepcionServicio("No se ha encontrado el comercio solicitado.");
        }
    }

    @Transactional
    public void darPuntos(Integer cantidad, String mailCliente, String mailComercio) throws ExcepcionServicio {

        if (cantidad == null || cantidad < 0) {
            throw new ExcepcionServicio("La cantiad de puntos debe ser mayor a 0");
        }
        Boolean encontrado = false;

        Cliente cliente = servicioCliente.buscarPorId(mailCliente);
        Comercio comercio = buscarPorId(mailComercio);

        for (Puntos punto : cliente.getPuntos()) {

            if (punto.getComercio().getMail().equals(mailComercio)) {

                Integer cantidadFinal = punto.getCantidad() + cantidad;
                
                punto.setCantidad(cantidadFinal);
                encontrado = true;

                puntosRepositorio.save(punto);
                clienteRepositorio.save(cliente);

                break;
            }
        }
        if (encontrado == false) {

            Puntos punto = new Puntos();

            punto.setCantidad(cantidad);
            punto.setComercio(comercio);

            cliente.getPuntos().add(punto);

            puntosRepositorio.save(punto);
            clienteRepositorio.save(cliente);

        }
        
    }



/**
 * Este método lo utilizo para poder validar el comercio que quiero registrar.
 * Todavía está incompleto, quizá podemos agregarle más adelante una segunda
 * clave a los métodos cuando se llene el formulario de front y confirmar que la
 * clave se repitió
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

    private void validarModificar(String mail, String nombreComercio, String nombre, String apellido, String telefono, String direccion, String rubros) throws ExcepcionServicio {

        if (mail == null || mail.isEmpty()) {
            throw new ExcepcionServicio("El mail no puede ser nulo o estar vacío.");
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

    }

}
