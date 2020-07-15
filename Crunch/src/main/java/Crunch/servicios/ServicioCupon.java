
package Crunch.servicios;

import Crunch.entidades.Cupon;
import java.util.Calendar;
import static org.apache.logging.log4j.ThreadContext.isEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicioCupon{
    @Autowired
    // todavia no estaban subidos los repositorios y el lauta me dijo que haga las excepciones pero que las deje comentadas.
    //private CuponRepositorio cuponRepositorio;
    public void crear(String id,String titulo,String descripcion, Calendar vencimiento){ //throws exceptionServicio{
        if(id == null || id.isEmpty()){
            //new throw exceptionServicio("la identificacion no puede estar vacia");
       
    }
        if(titulo == null || titulo.isEmpty()){//throws exceptionServicio
        
        //}
            //new throw exceptionServicio("el titulo no puede ser nulo");
       
    }
        if(descripcion == null || descripcion.isEmpty()|| descripcion.length()<= 15){
            //new throw exceptionServicio("la descripcion no puede estar vacia y tiene que tener mas de 15 digitos");
       
    }
        //----------------------FALTA EL VENCIMIENTO QUE NO SUPE COMO HACERLO----------------------------------
        
        Cupon cupon = new Cupon();
        cupon.setId(id);
        cupon.setTitulo(titulo);
        cupon.setDescripcion(descripcion);
        cupon.setVencimiento(vencimiento);
        
        //cuponRepositorio.save(cupon)
    }
    public void usar(){
        // muy complejo para mi! :(
    }

    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  

