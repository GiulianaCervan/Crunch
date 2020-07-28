package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Raspadita;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import Crunch.repositorios.RaspaditaRepositorio;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioRaspadita {

    @Autowired
    private ComercioRepositorio comercioRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private RaspaditaRepositorio raspaditaRepositorio;

    /**
     * Este método crea una lista de objetos Raspadita y se las setea a un
     * comercio.
     *
     * @param mailComercio
     * @param puntuacion
     * @param numero
     * @throws ExcepcionServicio
     */
    @Transactional
    public void crear(String mailComercio, Integer puntuacion, Integer cantidad) throws ExcepcionServicio {
        validarCrear(mailComercio, puntuacion, cantidad);

        Optional<Comercio> respuesta = comercioRepositorio.findById(mailComercio);

        if (respuesta.isPresent()) {
            Comercio comercio = respuesta.get();

            for (int i = 0; i < cantidad; i++) {
                Raspadita raspadita = new Raspadita();

                raspadita.setPuntuacion(puntuacion);
                comercio.getRaspaditas().add(raspadita);

                raspaditaRepositorio.save(raspadita);
            }
            comercioRepositorio.save(comercio);
        } else {
            throw new ExcepcionServicio("No se ha encontrado el comercio.");
        }
    }

    /**
     * Este método buscaría entre las raspaditas de los comercions y selecciona
     * uno pseudo-aleatoriamente y se lo asigna a un cliente.
     *
     * @param mailCliente
     * @param mailComercio
     * @throws ExcepcionServicio
     */
    @Transactional
    public void Otorgar(String mailCliente, String mailComercio) throws ExcepcionServicio {
        validarOtorgar(mailCliente,mailComercio);

        Optional<Cliente> respuestaCliente = clienteRepositorio.findById(mailCliente);
        Optional<Comercio> respuestaComercio = comercioRepositorio.findById(mailComercio);

        if (respuestaCliente.isPresent() && respuestaComercio.isPresent()) {

            Cliente cliente = respuestaCliente.get();
            Comercio comercio = respuestaComercio.get();
            Raspadita raspadita = raspaditaRandom(comercio);

            cliente.getRaspaditas().add(raspadita);
            comercio.getRaspaditas().remove(raspadita);

            clienteRepositorio.save(cliente);
            comercioRepositorio.save(comercio);

        } else if (!respuestaCliente.isPresent() && respuestaComercio.isPresent()) {
            throw new ExcepcionServicio("No se ha encontrado el cliente solicitado.");
        } else if (respuestaCliente.isPresent() && !respuestaComercio.isPresent()) {
            throw new ExcepcionServicio("No se ha encontrado el comercio solicitado.");
        } else {
            throw new ExcepcionServicio("Error inesperado.");
        }
    }

    /**
     * Este es un método que utiliza una función matemática pseudo-random para
     * elegir una raspadita entre la List<Raspadita> que tiene la entidad
     * Comercio.
     *
     * @param comercio
     * @return
     * @throws ExcepcionServicio
     */
    private Raspadita raspaditaRandom(Comercio comercio) throws ExcepcionServicio {

        Raspadita raspadita;
        List<Raspadita> raspaditas = comercio.getRaspaditas();
        Random random = new Random();
        Integer tamaño = comercio.getRaspaditas().size();
        Integer numeroRandom;

        if (tamaño == 0) {
            throw new ExcepcionServicio("No tiene raspaditas para otorgar.");
        }

        numeroRandom = random.nextInt(tamaño);

        raspadita = raspaditas.get(numeroRandom);

        return raspadita;
    }

    /**
     * Este método le permitiría usar la raspadita que le tocó a nuestro cliente
     * y luego de otorogarle los puntos que corresponden al cliente, elimina la
     * raspadita de la base de datos.
     * @param mailCliente
     * @param raspaditaId
     * @throws ExcepcionServicio 
     */
    @Transactional
    public void usarRaspadita(String mailCliente, String raspaditaId) throws ExcepcionServicio {
        validarUsar(mailCliente, mailCliente);
        
        Optional<Cliente> respuestaCliente = clienteRepositorio.findById(mailCliente);
        
        if(respuestaCliente.isPresent()){
            Cliente cliente = respuestaCliente.get();
            
            for (Raspadita raspadita : cliente.getRaspaditas()) {
                if(raspadita.getId().equals(raspaditaId)){
                    
                    Integer puntos = cliente.getPuntos();
                    
                    puntos += raspadita.getPuntuacion();
                    cliente.setPuntos(puntos);
                    
                    cliente.getRaspaditas().remove(raspadita);
                    
                    clienteRepositorio.save(cliente);
                    raspaditaRepositorio.delete(raspadita);
                    break;
                }
            }
        }else {
            throw new ExcepcionServicio("No se ha encontrado el cliente solicitado.");
        }
    }

    private void validarOtorgar(String mailCliente,String mailComercio) throws ExcepcionServicio {

        if (mailCliente == null || mailCliente.isEmpty()) {
            throw new ExcepcionServicio("El mail del cliente no puede ser nulo ni estar vacio");
        }

        if (mailComercio == null || mailComercio.isEmpty()) {
            throw new ExcepcionServicio("El mail del comercio no puede ser nulo ni estar vacio");
        }
    }

    private void validarCrear(String mailComercio, Integer puntuacion, Integer cantidad) throws ExcepcionServicio {

        if (mailComercio == null || mailComercio.isEmpty()) {
            throw new ExcepcionServicio("El mail del usuario no puede estar vacío ni ser nulo.");
        }

        if (puntuacion == null) {
            throw new ExcepcionServicio("La puntuación no puede ser nula.");
        }

        if (cantidad == null || cantidad < 1) {
            throw new ExcepcionServicio("La cantidad de raspaditas no puede ser"
                    + " ni menor a 1");
        }
    }

    private void validarUsar(String mailCliente,String idCupon) throws ExcepcionServicio{
        if (mailCliente == null || mailCliente.isEmpty()) {
            throw new ExcepcionServicio("El mail del cliente no puede ser nulo ni estar vacio");
        }
        if (idCupon == null || idCupon.isEmpty()) {
            throw new ExcepcionServicio("El id del cupon no puede ser nulo ni estar vacio.");
        }
    }
}
