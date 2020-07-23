
package Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/comercio")
public class ComercioControlador {
    
    @Autowired
    
    
    @GetMapping ("/ingresar comercio")
    private String comercio(){
        return "comercio.html";
    }
   @GetMapping("/listaractivos")
    private String activos(){
        return "activos.html";
    }
        
    @GetMapping ("/Login")
      private String Loging(){
       return "Loging.html";
          
      }
}
    
    



    