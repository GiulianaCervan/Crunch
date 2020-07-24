package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.RubroAsignado;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioComercio;
import Crunch.utilidades.Rubro;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

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

        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        
        String userMail = userDetails.getUsername();
        
        String rol = userDetails.getAuthorities().toString();
        System.out.println(userDetails);
        System.out.println(rol);
        switch(rol){
            
            case "[ROLE_CLIENTE]":
                Cliente cliente = null;
                try {
                    cliente = servicioCliente.buscarPorId(userMail);
                } catch (ExcepcionServicio e) {
                    modelo.put("error", e.getMessage());
                }
                
                session.setAttribute("usuariosession", cliente);
                return "inicioCliente.html";
                
                
            case "[ROLE_COMERCIO]":
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
//    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String documento, @RequestParam String nombre,@RequestParam String apellido, @RequestParam String domicilio,@RequestParam String telefono, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona){
//        Cliente cliente = null;
//        
//        try {
//            cliente = clienteRepositorio.buscarClientePorId(documento);
//            clienteServicio.modificar(documento, nombre, apellido, domicilio, telefono, clave1, clave2, idZona);
//            session.setAttribute("usuariosession", cliente);
//            return "redirect:/perfil";
//            
//        } catch (ErrorServicio e) {
//      
//            List<Zona> zonas = zonaRepositorio.findAll();
//            modelo.put("zonas", zonas);
//            modelo.put("error", e.getMessage());           
//            modelo.put("perfil", cliente);
//           
//            return "error.html";
//        }
//    }

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
    public String registrar(ModelMap modelo,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String domicilio,@RequestParam String telefono) {
        try {
            servicioCliente.crear(mail, clave1, clave2, nombre, apellido, domicilio, telefono);
        } catch (ExcepcionServicio e) {

            modelo.put("error", e.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            modelo.put("domicilio",domicilio);
            
            return "registro.html";
        }
        return "exito.html";
    }
   
    @GetMapping("/registro-comercio")
    public String registroComercio(ModelMap modelo){
        modelo.put("rubros", Rubro.values());
        return "registroComercio.html";
    }
    
    @PostMapping("/registrar-comercio")
    public String registrarComercio(ModelMap modelo,MultipartFile archivo,@RequestParam String mail,@RequestParam String clave,@RequestParam String clave2,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String telefono,@RequestParam String direccion, @RequestParam String nombreComercio, @RequestParam String rubros){
        try {
            System.out.println("------------------------------rubros:" + rubros);
            servicioComercio.crear(archivo,mail, clave, clave2, nombre, apellido, telefono, direccion, nombreComercio, rubros);
            
        } catch (ExcepcionServicio e) {
            modelo.put("error",e.getMessage());
            
            modelo.put("mail",mail);
            modelo.put("nombre",nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono",telefono);
            modelo.put("direccion", direccion);
            modelo.put("nombreComercio", nombreComercio);
            /**
             * modelo.put("rubros",rubros); Revisar el imput de los rubron en el front
             */
            
            return "registroComercio";
        }
        return "exito.html";
    }

}
