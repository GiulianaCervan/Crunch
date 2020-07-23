
package Controlador;

import Crunch.excepciones.ExcepcionServicio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author JULIETA
 */
@Controller
@RequestMapping("/")
public class PortalControlador {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    @GetMapping("/Login")
    public String Login(){
    return "Loging.html";
}
    @GetMapping("/registro")
    public String Registro(){
        return "Registro.html";
    }
    @PostMapping("/registrar")
    //@RequestParam
    public String registrar(String mail, String clave,String clave2, String nombre, String apellido, String domicilio, String telefono){
        return "registro.html";
    }
}
