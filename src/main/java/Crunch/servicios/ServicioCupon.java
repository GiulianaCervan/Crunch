/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
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

    /**
     * Pide la informacion del cliente que esta logueado, busco al cliente,
     * itera en base a la cantidad de cupones que se quieren crear, asignandoles
     * a sus campos los mismos valores, los agrega a la lista de cuponesPromo
     * del cliente y persiste en BD a los cupones y al cliente con las
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

        for (Cupon cupon : comercio.getCuponesPromo()) {
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
                comercio.getCuponesPromo().add(cupon);
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

        Comercio comercio = repositorioComercio.getOne(titulo);

        for (Cupon cupon : comercio.getCuponesPromo()) {
            if (cupon.getTitulo().equals(titulo) && cupon.isDisponible()) {
                comercio.getCuponesPromo().remove(cupon);
                repositorioCupon.delete(cupon);
            }
        }
        repositorioComercio.save(comercio);
    }

    /**
     * Busca el cupon en la BD, lo setea como no disponible, se lo agrega a
     * lista de CuponesPromo del cliente logueado, y persisite los cambios.
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
        cliente.getCuponPromo().add(cupon);

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

        Comercio comercio = repositorioComercio.getOne(idCupon);

        for (Cupon cupon : comercio.getCuponesPromo()) {

            if (cupon.getId().equals(idCupon)) {
                if (!cupon.isVencido()) {
                    Cliente cliente = cupon.getCliente();

                    cliente.getCuponPromo().remove(cupon);
                    comercio.getCuponesPromo().remove(cupon);

                    repositorioCupon.delete(cupon);
                    repositorioCliente.save(cliente);
                    repositorioComercio.save(comercio);
                    break;
                } else {
                    throw new ExcepcionServicio("El cupon no se puede canjear, esta vencido");
                }

            }
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

            for (Cupon cupon : cliente.getCuponPromo()) {

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

                cliente.getCuponPromo().remove(cupon);
                comercio.getCuponesPromo().remove(cupon);

                repositorioCupon.delete(cupon);
                repositorioCliente.save(cliente);
                repositorioComercio.save(comercio);
            }
        } else if (respuestaComercio.isPresent()) {

            Comercio comercio = respuestaComercio.get();

            for (Cupon cupon : comercio.getCuponesPromo()) {

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

                cliente.getCuponPromo().remove(cupon);
                comercio.getCuponesPromo().remove(cupon);

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
    public List<Cupon> mostrarBanners(String rubro) {

        List<Cupon> cupones = mostrarPorRubros(rubro);
        List<Cupon> banners = new ArrayList<>();
        banners.add(cupones.get(0));
        Boolean encontrado = false;

        for (Cupon cupon : cupones) {

            for (Cupon banner : banners) {

                if ((cupon.getTitulo().equals(banner.getTitulo())) && cupon.getComercio().equals(banner.getComercio())) {
                    encontrado = false;
                    break;
                } else {

                    encontrado = true;

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
     * Validador generico
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
}
