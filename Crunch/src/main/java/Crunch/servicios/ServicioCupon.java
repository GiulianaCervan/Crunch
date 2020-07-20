/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Cupon;
import Crunch.repositorios.RepositorioCupon;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
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

    public void crear(String titulo, String descripcion, Integer semanas, String mailComercio, Integer cantidad) throws ExcepcionServicio {

        validar(titulo, descripcion);

        if (cantidad > 0) {
            for (int i = 0; i < cantidad; i++) {

                Cupon cupon = new Cupon();

                cupon.setTitulo(titulo);
                cupon.setDescripcion(descripcion);
                cupon.setMailComercio(mailComercio);

                if (semanas > 0) {

                    Calendar fecha = Calendar.getInstance();
                    fecha.add(Calendar.WEEK_OF_YEAR, semanas);
                    cupon.setVencimiento(fecha);

                }

                repositorioCupon.save(cupon);

            }
        } else {
            throw new ExcepcionServicio("La canitdad de cupones a crear no puede ser menor a 1");
        }
    }
    

    
    public void borrar(String id) throws ExcepcionServicio{
        
        Optional<Cupon> respuesta = repositorioCupon.findById(id);
        
        if (respuesta.isPresent()) {
             if (respuesta.get().isDisponible()) {
                repositorioCupon.delete(respuesta.get());
            }
            
        }else{
            throw new ExcepcionServicio("No se puede borrar un cupon ya otorgado");
        }
    }
    
    public void otorgar(String mailCliente, String idCupon) throws ExcepcionServicio{
        
        validar(mailCliente, idCupon);
        
        Optional<Cupon> respuesta = repositorioCupon.findById(idCupon);
        Cupon cupon = null;
        if (respuesta.isPresent()) {
            
            cupon = respuesta.get();
        }else{
            throw new ExcepcionServicio("No se encontro el cupon");
        }
        
        Cliente cliente = repositorioCliente.findById(idCupon).get();
        cupon.setDisponible(false);
        cliente.getCuponPromo().add(cupon);
        
        repositorioCupon.save(cupon);
        
    }
   
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
