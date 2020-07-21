package Crunch.controladores;

import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ControladorPortal {

    @Autowired
    private ServicioCliente servicioCliente;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false)String error,@RequestParam(required = false)String logout, ModelMap modelo){
        if(error != null){
            modelo.put("error","Usuario o clave incorrectos.");
        }
        if(logout != null){
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return"loginComercio.html";
    }
    
    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String domicilio,@RequestParam String telefono){
        try {
            servicioCliente.crear(mail, clave2, clave2, nombre, apellido, domicilio, telefono);
        } catch (ExcepcionServicio e) {
            
            modelo.put("error",e.getMessage());
            
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono",telefono);
            modelo.put("mail",mail);
            /***************************FALTA PONER domicilio EN EL REGISTRO*/
//            modelo.put("domicilio",domicilio);
            return"registro.html";
        }
        return"exito.html";
    }
}
