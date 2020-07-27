package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioComercio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private ServicioComercio servicioComercio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_COMERCIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        System.out.println("Llegue al controladorrrrrrrrrrrrrrrrrrrrrrrr");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        
        String userMail = userDetails.getUsername();
        
        String rol = userDetails.getAuthorities().toString();
        System.out.println(userDetails);
        System.out.println(rol);
        switch(rol){
            
            case "ROLE_CLIENTE":
                Cliente cliente = null;
                try {
                    cliente = servicioCliente.buscarPorId(userMail);
                } catch (ExcepcionServicio e) {
                    modelo.put("error", e.getMessage());
                }
                
                session.setAttribute("usuariosession", cliente);
                return "inicioCliente.html";
                
            case "ROLE_COMERCIO":
                Comercio comercio = null;
                try {
                    comercio = servicioComercio.buscarPorId(userMail);                    
                } catch (ExcepcionServicio e) {
                    modelo.put("error", e.getMessage());
                }
                session.setAttribute("usuariosession", comercio);
                    return "inicioComercio.html";
            default:
                modelo.put("error", "Algo a pasado...");
                return "index.html";
        }
        
        
        

    }
//    

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o clave incorrectos.");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro.html";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, @RequestParam(required = false) String mail, @RequestParam(required = false) String clave1, @RequestParam(required = false) String clave2, @RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido, @RequestParam(required = false) String domicilio, @RequestParam(required = false) String telefono, @RequestParam(required = false) String area) {
        try {
            area.concat(telefono);
            
            servicioCliente.crear(mail, clave1, clave2, nombre, apellido, domicilio, area);
        } catch (ExcepcionServicio e) {

            modelo.put("error", e.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            /**
             * *************************FALTA PONER domicilio EN EL REGISTRO en el front!
             */
//            modelo.put("domicilio",domicilio);
            return "registro.html";
        }
        return "exito.html";
    }
}
