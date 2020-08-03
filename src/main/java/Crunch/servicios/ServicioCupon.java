/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
import Crunch.entidades.Puntos;
import Crunch.entidades.RubroAsignado;
import Crunch.repositorios.RepositorioCupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import Crunch.utilidades.TipoCupon;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author duili
 */
@Service
public class ServicioCupon {

    @Autowired
    private RepositorioCupon repositorioCupon;
    @Autowired
    private ClienteRepositorio repositorioCliente;
    @Autowired
    private ComercioRepositorio repositorioComercio;

    @Autowired
    private ServicioCliente servicioCliente;

    @Autowired
    private ServicioComercio servicioComercio;

    /**
     * Este método encuentra un cupon cuando se le dá el id.
     *
     * @param id
     * @return
     * @throws ExcepcionServicio
     */
    public Cupon buscarCuponPorId(String id) throws ExcepcionServicio {

        if (id == null || id.isEmpty()) {
            throw new ExcepcionServicio("El campo id no puede estar vacio o ser nulo");
        }

        Optional<Cupon> respuesta = repositorioCupon.findById(id);
        if (respuesta.isPresent()) {
            Cupon cupon = respuesta.get();
            return cupon;
        } else {
            throw new ExcepcionServicio("El cupón ingresado no se ha encontrado"
                    + "o no está disponible");
        }
    }

    /**
     * Pide la informacion del cliente que esta logueado, busco al cliente,
     * itera en base a la cantidad de cupones que se quieren crear, asignandoles
     * a sus campos los mismos valores, los agrega a la lista de cupones del
     * cliente y persiste en BD a los cupones y al cliente con las
     * actualizaciones
     *
     * @param titulo
     * @param descripcion
     * @param vencimiento
     * @param mailComercio
     * @param cantidad
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(String titulo, String descripcion, String vencimiento, String mailComercio, Integer cantidad) throws ExcepcionServicio {

        validar(titulo, descripcion);

        Comercio comercio = repositorioComercio.findById(mailComercio).get();

        for (Cupon cupon : comercio.getCupones()) {
            if (cupon.getTitulo().equals(titulo)) {
                throw new ExcepcionServicio("Ya tiene creado un cupon con ese Titulo");
            }
        }
        if (cantidad > 0) {
            for (int i = 0; i < cantidad; i++) {

                Cupon cupon = new Cupon();

                cupon.setTitulo(titulo);
                cupon.setDescripcion(descripcion);
                cupon.setComercio(comercio);
                cupon.setTipo(TipoCupon.PROMOCION);

                Calendar minimo = Calendar.getInstance();
                minimo.add(Calendar.DAY_OF_WEEK, 7);
                Calendar fechaC = Calendar.getInstance();
                minimo.add(Calendar.DAY_OF_MONTH, 7);

                String[] fecha = vencimiento.split("-");

                fechaC.set(Calendar.YEAR, Integer.parseInt(fecha[0]));
                fechaC.set(Calendar.MONTH, Integer.parseInt(fecha[1]));
                fechaC.set(Calendar.DAY_OF_WEEK, Integer.parseInt(fecha[2]));

                if (fechaC.before(minimo)) {
                    throw new ExcepcionServicio("La fecha minima de duracion de un cupon es de una semana");
                }

                cupon.setVencimiento(new java.sql.Date(fechaC.getTimeInMillis()));
                comercio.getCupones().add(cupon);
                repositorioCupon.save(cupon);

            }
            repositorioComercio.save(comercio);
        } else {
            throw new ExcepcionServicio("La cantidad de cupones a crear no puede ser menor a 1");
        }
    }

    /**
     * Pide la informacion del cliente que esta logueado, busco al cliente,
     * itera en base a la cantidad de cupones que se quieren crear, asignandoles
     * a sus campos los mismos valores, los agrega a la lista de cupones del
     * cliente y persiste en BD a los cupones y al cliente con las
     * actualizaciones, PERO estas tienen un costo.
     *
     * @param titulo
     * @param descripcion
     * @param vencimiento
     * @param mailComercio
     * @param costo
     * @param cantidad
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crearCuponCanje(String titulo, String descripcion, String vencimiento, String mailComercio, Integer costo, Integer cantidad) throws ExcepcionServicio {

        validarCuponCanje(titulo, descripcion, costo);

        Comercio comercio = repositorioComercio.findById(mailComercio).get();

        for (Cupon cupon : comercio.getCupones()) {
            if (cupon.getTitulo().equals(titulo)) {
                throw new ExcepcionServicio("Ya tiene creado un cupon con ese Titulo");
            }
        }
        if (cantidad > 0) {
            for (int i = 0; i < cantidad; i++) {

                Cupon cupon = new Cupon();

                cupon.setTitulo(titulo);
                cupon.setDescripcion(descripcion);
                cupon.setComercio(comercio);
                cupon.setCosto(costo);
                cupon.setTipo(TipoCupon.CANJE);

                Calendar minimo = Calendar.getInstance();
                minimo.add(Calendar.DAY_OF_WEEK, 7);
                Calendar fechaC = Calendar.getInstance();
                minimo.add(Calendar.DAY_OF_MONTH, 7);

                String[] fecha = vencimiento.split("-");

                fechaC.set(Calendar.YEAR, Integer.parseInt(fecha[0]));
                fechaC.set(Calendar.MONTH, Integer.parseInt(fecha[1]));
                fechaC.set(Calendar.DAY_OF_WEEK, Integer.parseInt(fecha[2]));

                if (fechaC.before(minimo)) {
                    throw new ExcepcionServicio("La fecha minima de duracion de un cupon es de una semana");
                }

                cupon.setVencimiento(new java.sql.Date(fechaC.getTimeInMillis()));
                comercio.getCupones().add(cupon);
                repositorioCupon.save(cupon);

            }
            repositorioComercio.save(comercio);
        } else {
            throw new ExcepcionServicio("La cantidad de cupones a crear no puede ser menor a 1");
        }
    }

    /**
     * Busco al comercio logueado, bsuco en su lista de cuponesPromo a los que
     * coincidan con el titulo, verifico que no haya sido otorgados y borro si
     * cumplen ambas condiciones
     *
     * @param titulo
     * @param mailComercio
     * @throws ExcepcionServicio
     */
    @Transactional
    public void borrar(String titulo, String mailComercio) throws ExcepcionServicio {

        validar(titulo, mailComercio);

        Comercio comercio = repositorioComercio.getOne(mailComercio);

        Iterator<Cupon> iterador = comercio.getCupones().iterator();
        for (int i = 0; i < comercio.getCupones().size(); i++) {
            for (Cupon cupon : comercio.getCupones()) {
                if (cupon.getTitulo().equals(titulo) && cupon.isDisponible()) {
                    comercio.getCupones().remove(cupon);
                    repositorioCupon.delete(cupon);
                    break;
                }
            }

        }

        repositorioComercio.save(comercio);
    }

    /**
     * Busca el cupon en la BD, lo setea como no disponible, se lo agrega a
     * lista de Cupones del cliente logueado, y persisite los cambios.
     *
     * @param mailCliente
     * @param idCupon
     * @throws ExcepcionServicio
     */
    @Transactional
    public void otorgar(String mailCliente, String idCupon) throws ExcepcionServicio {

        validar(mailCliente, idCupon);

        Optional<Cupon> respuesta = repositorioCupon.findById(idCupon);
        Cupon cupon = null;
        if (respuesta.isPresent()) {
            cupon = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se encontro el cupon");
        }
        Cliente cliente = repositorioCliente.getOne(mailCliente);

        cupon.setDisponible(false);
        cupon.setCliente(cliente);
        cliente.getCupones().add(cupon);

        repositorioCupon.save(cupon);
        repositorioCliente.save(cliente);

    }

    /**
     * Este método busca el cliente en la base de datos, si este tiene los
     * puntos necesarios para adquirir el cupon entonces se lo asigna cliente
     *
     * @param mailCliente
     * @param idCupon
     * @throws ExcepcionServicio
     */
    @Transactional
    public void otorgarCuponCanje(String mailCliente, String idCupon) throws ExcepcionServicio {

        validar(mailCliente, idCupon);

        Optional<Cupon> respuesta = repositorioCupon.findById(idCupon);
        Cupon cupon = null;
        if (respuesta.isPresent()) {
            cupon = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se encontro el cupon");
        }
        Cliente cliente = repositorioCliente.getOne(mailCliente);
        List<Puntos> puntos = cliente.getPuntos();

        validarPuntos(cupon.getCosto(), servicioCliente.puntosPorComercio(mailCliente, cupon.getComercio().getMail()));

        for (Puntos punto : puntos) {
            if (punto.getComercio().equals(cupon.getComercio())) {
                Integer puntoDespuesCompra = punto.getCantidad() - cupon.getCosto();

                punto.setCantidad(puntoDespuesCompra);
            }
        }

        cupon.setDisponible(false);
        cupon.setCliente(cliente);

        cliente.getCupones().add(cupon);

        repositorioCupon.save(cupon);
        repositorioCliente.save(cliente);

    }

    /**
     * Busca el cupon por id, busca en los cupones del comercio, luego lo busca
     * en el cliente y lo borra de ambos.. validando el cupon y guardando los
     * cambios en la BD
     *
     * @param mailComercio
     * @param idCupon
     * @throws Crunch.excepciones.ExcepcionServicio
     */
    @Transactional
    public void validarCupon(String mailComercio, String idCupon) throws ExcepcionServicio {

        Comercio comercio = servicioComercio.buscarPorId(mailComercio);
        Boolean encontrado = false;
        for (Cupon cupon : comercio.getCupones()) {

            if (cupon.getId().substring(24).equals(idCupon)) {
                encontrado = true;
                if (!cupon.isVencido()) {
                    if (!cupon.isDisponible()) {

                        Cliente cliente = cupon.getCliente();

                        cliente.getCupones().remove(cupon);
                        comercio.getCupones().remove(cupon);

                        repositorioCupon.delete(cupon);
                        repositorioCliente.save(cliente);
                        repositorioComercio.save(comercio);

                        break;
                    } else {
                        throw new ExcepcionServicio("El cupon no ha sido otorgado a nadie");
                    }
                } else {
                    throw new ExcepcionServicio("El cupon no se puede canjear, esta vencido");
                }

            }
        }
        if (!encontrado) {
            throw new ExcepcionServicio("No se encontro un cupon con ese ID en sus cupones");
        }

    }

    /**
     * Metodo que debemos usar cada vez que un usuario se loguea... busca en su
     * lista de cupones si tiene cupones vencidos o los pone como tal, si el
     * cupon lleva mas de 7 dias vencidos, los borra
     *
     * @param mail
     */
    @Transactional
    public void verificarVencidos(String mail) {

        Optional<Cliente> respuestaCliente = repositorioCliente.findById(mail);
        Optional<Comercio> respuestaComercio = repositorioComercio.findById(mail);

        Calendar hoy = Calendar.getInstance();
        Calendar semana = Calendar.getInstance();

        List<Cupon> aBorrar = new ArrayList<>();
        if (respuestaCliente.isPresent()) {

            Cliente cliente = respuestaCliente.get();

            for (Cupon cupon : cliente.getCupones()) {

                semana.setTime(cupon.getVencimiento());
                semana.add(Calendar.DAY_OF_MONTH, 7);

                if (hoy.after(cupon.getVencimiento())) {
                    cupon.setVencido(true);
                    repositorioCupon.save(cupon);
                }
                if (semana.after(cupon.getVencimiento())) {

                    aBorrar.add(cupon);

                }
            }

            for (Cupon cupon : aBorrar) {
                Comercio comercio = cupon.getComercio();

                cliente.getCupones().remove(cupon);
                comercio.getCupones().remove(cupon);

                repositorioCupon.delete(cupon);
                repositorioCliente.save(cliente);
                repositorioComercio.save(comercio);
            }
        } else if (respuestaComercio.isPresent()) {

            Comercio comercio = respuestaComercio.get();

            for (Cupon cupon : comercio.getCupones()) {

                semana.setTime(cupon.getVencimiento());
                semana.add(Calendar.DAY_OF_MONTH, 7);

                if (hoy.after(cupon.getVencimiento())) {
                    cupon.setVencido(true);
                    repositorioCupon.save(cupon);
                }
                if (semana.after(cupon.getVencimiento())) {

                    aBorrar.add(cupon);

                }

            }

            for (Cupon cupon : aBorrar) {

                Cliente cliente = cupon.getCliente();

                cliente.getCupones().remove(cupon);
                comercio.getCupones().remove(cupon);

                repositorioCupon.delete(cupon);
                repositorioCliente.save(cliente);
                repositorioComercio.save(comercio);
            }

        }

    }

    /**
     * Busca en todos los cupones y genera los banners en funcion de sus titulos
     * y sus comercios
     *
     * @param rubro
     * @return
     */
    public List<Cupon> mostrarBanners(String rubro) throws ExcepcionServicio {

        List<Cupon> cupones = mostrarPorRubros(rubro);
        List<Cupon> banners = new ArrayList<>();
        Integer cant = 0;
        try {
            do {                
                 if (cupones.get(cant).isDisponible()) {
                banners.add(cupones.get(cant));
            }else{
                cant++;
            }
            } while (banners.isEmpty());
           
                
            
        } catch (Exception e) {
            throw new ExcepcionServicio("Lo lamentos por el momento no quedan disponibles cupones para este rubro");
        }

        Boolean encontrado = false;

        for (Cupon cupon : cupones) {

            for (Cupon banner : banners) {

                if (cupon.isDisponible()) {

                    if ((cupon.getTitulo().equals(banner.getTitulo())) && cupon.getComercio().equals(banner.getComercio())) {
                        encontrado = false;
                        break;
                    } else {

                        encontrado = true;

                    }
                }
            }

            if (encontrado == true) {
                banners.add(cupon);
            }
            encontrado = false;
        }

        return banners;

    }

    /**
     * Busca. dentro de la lista del comercio un cupon que no este otorgado y
     * devuelve su id
     *
     * @param titulo
     * @param comercio
     * @return
     * @throws ExcepcionServicio
     */
    public String buscarCuponDisponible(String titulo, String comercio) throws ExcepcionServicio {

        List<Cupon> cupones = repositorioCupon.buscarPorTituloyComercio(titulo, comercio);

        for (Cupon cupon : cupones) {

            if (cupon.isDisponible()) {
                return cupon.getId();
            }
        }
        throw new ExcepcionServicio("Lo lamentamos, ya no quedan cupones disponibles");
    }

    public List<Cupon> buscarPorTituloyComercio(String titulo, String mailComercio) {

        List<Cupon> cupones = repositorioCupon.buscarPorTituloyComercio(titulo, mailComercio);

        return cupones;
    }

    public List<Cupon> mostrarCuponesCliente(String mailCliente) {

        Cliente cliente = repositorioCliente.getOne(mailCliente);

        return cliente.getCupones();
    }

    public List<Cupon> mostrarPorComercio(String mailComercio) {

        return repositorioCupon.buscarPorComercioBanner(mailComercio);

    }

    /**
     * Pide el rubro de los cupones a filtrar, en caso de no tenerlo, devuelve
     * todos los cupones de la base de datos
     *
     * @param rubro
     * @return
     */
    private List<Cupon> mostrarPorRubros(String rubro) {

        List<Cupon> todos = repositorioCupon.findAll();
        List<Cupon> porRubro = new ArrayList<>();
        if (rubro == null || rubro.isEmpty()) {
            return todos;
        }
        for (Cupon cupon : todos) {

            for (RubroAsignado rubroComercio : cupon.getComercio().getRubros()) {

                if (rubroComercio.getRubro().name().equals(rubro)) {

                    porRubro.add(cupon);
                }
            }
        }

        return porRubro;
    }

    /**
     * Validador para los cupones genéricos sin costo.
     *
     * @param titulo
     * @param descripcion
     * @throws ExcepcionServicio
     */
    private void validar(String titulo, String descripcion) throws ExcepcionServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ExcepcionServicio("El titulo no puede ser nulo o estar vacio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new ExcepcionServicio("La descripcion no puede ser nulo o estar vacio");
        }
    }

    /**
     * Validador para los cupones que tienen un costo en puntos.
     *
     * @param titulo
     * @param descripcion
     * @param costo
     * @throws ExcepcionServicio
     */
    private void validarCuponCanje(String titulo, String descripcion, Integer costo) throws ExcepcionServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ExcepcionServicio("El titulo no puede ser nulo o estar vacio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new ExcepcionServicio("La descripcion no puede ser nulo o estar vacio");
        }

        if (costo == null || costo <= 0) {
            throw new ExcepcionServicio("El valor de este cupón es inválido.");
        }

    }

    /**
     * Este método verifica si los puntos del cliente son sufucientes para
     * adquirir el cupon.
     *
     * @param costo
     * @param puntos
     * @throws ExcepcionServicio
     */
    private void validarPuntos(Integer costo, Integer puntos) throws ExcepcionServicio {
        if (puntos == 0) {
            throw new ExcepcionServicio("No tienes puntos de este comercio.");
        }
        if (costo > puntos) {
            throw new ExcepcionServicio("No tienes los suficientes puntos para adquirir este cupón.");
        }
    }
}
