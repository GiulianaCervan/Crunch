/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
import Crunch.repositorios.RepositorioCupon;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
     * Pide la informacion del cliente que esta logueado, busco al cliente, itera en base a la cantidad de cupones que se quieren
     * crear, asignandoles a sus campos los mismos valores, los agrega a la lista de cuponesPromo del cliente y persiste en BD a los 
     * cupones y al cliente con las actualizaciones
     * 
     * @param titulo
     * @param descripcion
     * @param dias
     * @param mailComercio
     * @param cantidad
     * @throws ExcepcionServicio 
     */
    public void crear(String titulo, String descripcion, Integer dias, String mailComercio, Integer cantidad) throws ExcepcionServicio {

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

                if (dias >= 7) {

                    Calendar fecha = Calendar.getInstance();
                    fecha.add(Calendar.DAY_OF_WEEK, dias);
                    cupon.setVencimiento(fecha);

                } else {
                    throw new ExcepcionServicio("La cantidad minima de dias para un cupon es de 7");
                }

                comercio.getCuponesPromo().add(cupon);
                repositorioCupon.save(cupon);

            }
            repositorioComercio.save(comercio);
        } else {
            throw new ExcepcionServicio("La cantidad de cupones a crear no puede ser menor a 1");
        }
    }
    /**
     * Busco al comercio logueado, bsuco en su lista de cuponesPromo a los que coincidan con el titulo, verifico que no haya sido 
     * otorgados y borro si cumplen ambas condiciones
     * 
     * @param titulo
     * @param mailComercio
     * @throws ExcepcionServicio 
     */
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
     * Busca el cupon en la BD, lo setea como no disponible, se lo agrega a lista de CuponesPromo del cliente logueado,
     * y persisite los cambios.
     * 
     * @param mailCliente
     * @param idCupon
     * @throws ExcepcionServicio 
     */
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

    public void validarCupon(String mailComercio, String idCupon) {

           Comercio comercio = repositorioComercio.getOne(idCupon);
           
           for (Cupon cupon : comercio.getCuponesPromo()) {
            
               if (cupon.getId().equals(idCupon)) {
                   
                   Cliente cliente = cupon.getCliente();
                   
                   cliente.getCuponPromo().remove(cupon);
                   comercio.getCuponesPromo().remove(cupon);
                   
                   repositorioCupon.delete(cupon);
                   repositorioCliente.save(cliente);
                   repositorioComercio.save(comercio);
               }
        }

    }
    
    public void verificarVencidos(String mailCliente){
        
        Cliente cliente = repositorioCliente.getOne(mailCliente);
        
        Calendar hoy = Calendar.getInstance();
        for (Cupon cupon : cliente.getCuponPromo()) {
            
            if (hoy.after(cupon.getVencimiento())) {
                cupon.setVencido(true);
                repositorioCupon.save(cupon);
            }
        }

    }

    //Borrado de cupones vencidos al hacer login
//Validador generico
    private void validar(String titulo, String descripcion) throws ExcepcionServicio {

        if (titulo == null || titulo.isEmpty()) {
            throw new ExcepcionServicio("El titulo no puede ser nulo o estar vacio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new ExcepcionServicio("La descripcion no puede ser nulo o estar vacio");
        }
    }
}
