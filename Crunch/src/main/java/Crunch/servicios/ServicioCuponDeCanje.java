
package Crunch.servicios;


import Crunch.entidades.CuponDeCanje;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioCuponDeCanje {
    @Autowired
    //los repositorios no estan hechos
    //private CuponDeCanjeRepositorio cuponDeCanjeRepositorio;
    
    public void crear(Integer costo,String id,String titulo,String descripcion,Calendar vencimiento){
        if(costo == null){//throws exceptionService
            //new throws exceptionServicio("el costo no puede ser nulo")
        }
        if (id == null){//throws exceptionService
            // new throws exceptionService ("el id no puede ser nulo")
            
        }
        if (titulo == null){// throws exceptionService
            //new exceptionService ("el titulo no puede ser nulo")
        }
        if (descripcion == null||descripcion.isEmpty()|| descripcion.length()<= 15){// throws ecxeptionService
            //new exceptionService("la descripcion no puede ser nula,estar vacia, y debe contener al menos 16 caracteres") "
        }
        //VENCIMIENTO NO LO SUPE HACER
        CuponDeCanje cuponDeCanje = new CuponDeCanje();
        cuponDeCanje.setCosto(costo);
        cuponDeCanje.setId(id);
        cuponDeCanje.setTitulo(titulo);
        cuponDeCanje.setDescripcion(descripcion);
        cuponDeCanje.setVencimiento(vencimiento);
        
        //CuponDeCanjeRepositorio.save(cuponDeCanje)
        
        
        
        
        
    }
    //METODO USAR
        public void usar(){
            
        }
        
    
    
    
    
}
