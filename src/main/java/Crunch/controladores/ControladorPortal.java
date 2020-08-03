package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Cupon;
import Crunch.entidades.Foto;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioComercio;
import Crunch.servicios.ServicioCupon;
import Crunch.servicios.ServicioFoto;
import Crunch.utilidades.Rubro;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private ServicioCupon servicioCupon;
    @Autowired
    private ServicioFoto servicioFoto;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/cupones/{rubro}")
    public String mostrarCupones(@PathVariable String rubro, ModelMap modelo) {

        List<Cupon> cupones = new ArrayList();
        try {
            cupones = servicioCupon.mostrarBanners(rubro);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }

        modelo.put("cupones", cupones);

        return "cupones.html";
    }

    @GetMapping("/cupones")
    public String mostrarTodosLosCupones(ModelMap modelo) {
        List<Cupon> cupones = new ArrayList();
        try {
            cupones = servicioCupon.mostrarBanners(null);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }

        modelo.put("cupones", cupones);
        return "cupones.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_COMERCIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String userMail = userDetails.getUsername();

        String rol = userDetails.getAuthorities().toString();

        switch (rol) {

            case "[ROLE_CLIENTE]":
                Cliente cliente = null;
                try {
                    cliente = servicioCliente.buscarPorId(userMail);
                    servicioCupon.verificarVencidos(userMail);
                    session.setAttribute("usuariosession", cliente);
                } catch (ExcepcionServicio e) {
                    modelo.put("error", e.getMessage());
                }

                servicioCupon.verificarVencidos(cliente.getMail());

                return "inicioCliente.html";

            case "[ROLE_COMERCIO]":
                Comercio comercio = null;
                try {
                    comercio = servicioComercio.buscarPorId(userMail);
                    servicioCupon.verificarVencidos(userMail);
                    session.setAttribute("usuariosession", comercio);
                } catch (ExcepcionServicio e) {
                    modelo.put("error", e.getMessage());
                }
                servicioCupon.verificarVencidos(comercio.getMail());
                modelo.addAttribute("comercio", comercio);

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

    public String registrar(ModelMap modelo, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String domicilio, @RequestParam String telefono) {

        try {
            servicioCliente.crear(mail, clave1, clave2, nombre, apellido, domicilio, telefono);
        } catch (ExcepcionServicio e) {

            modelo.put("error", e.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            modelo.put("domicilio", domicilio);

            return "registro.html";
        }
        return "exito.html";
    }

    @GetMapping("/registro-comercio")
    public String registroComercio(ModelMap modelo) {
        modelo.put("rubros", Rubro.values());
        return "registroComercio.html";
    }

    @PostMapping("/registrar-comercio")
    public String registrarComercio(ModelMap modelo, MultipartFile archivo, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono, @RequestParam String direccion, @RequestParam String nombreComercio, @RequestParam String rubros) {
        try {
            System.out.println("------------------------------rubros:" + rubros);
            servicioComercio.crear(archivo, mail, clave, clave2, nombre, apellido, telefono, direccion, nombreComercio, rubros);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());

            modelo.put("mail", mail);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("direccion", direccion);
            modelo.put("nombreComercio", nombreComercio);
            /**
             * modelo.put("rubros",rubros); Revisar el imput de los rubron en el
             * front
             */

            return "registroComercio.html";
        }
        return "exito.html";
    }

    @GetMapping("/cargar/{id}")
    public ResponseEntity<byte[]> cargarfoto(@PathVariable String id) {
        Foto foto = servicioFoto.buscarFoto(id);
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(foto.getMime())));
        return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
    }

}
