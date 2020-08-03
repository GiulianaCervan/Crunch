/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comercio")
public class ControladorComercio {

    @Autowired
    private ServicioComercio servicioComercio;
    @Autowired
    private ServicioCupon servicioCupon;
    @Autowired
    private ServicioCliente servicioCliente;
    @Autowired
    private ServicioFoto servicioFoto;

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/perfil")
    public String mostrarPerfil(ModelMap modelo, HttpSession session) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        Comercio comercio = null;
        try {
            comercio = servicioComercio.buscarPorId(userMail);
            session.setAttribute("usuariosession", comercio);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
        }
        return "";

    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/cupon")
    public String cupon() {
        return "cupon.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/crearCupon")
    public String crearCupon(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam Integer cantidad, ModelMap modelo,
             RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        try {

            servicioCupon.crear(titulo, descripcion, vencimiento, userMail, cantidad);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("vencimiento", vencimiento);
            modelo.put("cantidad", cantidad);

            return "cupon.html";
        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "error.html";
        }
        redirect.addFlashAttribute("exito", "Cupón de promoción creado correctamente");

        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/cuponPuntos")
    public String cuponPuntos() {

        return "cuponPuntos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/crearCuponPuntos")
    public String crearCuponPuntos(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam Integer costo,
            @RequestParam Integer cantidad, ModelMap modelo, RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        try {

            servicioCupon.crearCuponCanje(titulo, descripcion, vencimiento, userMail, costo, cantidad);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("vencimiento", vencimiento);
            modelo.put("costo", costo);
            modelo.put("cantidad", cantidad);

            return "cuponPuntos.html";
        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "error.html";
        }
        redirect.addFlashAttribute("exito", "Cupón por puntos creado correctamente");

        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/mostrarPerfil")
    public String mostrarPerfilComercio(HttpSession session, ModelMap modelo, RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        Comercio comercio = null;

        try {

            comercio = servicioComercio.buscarPorId(userMail);
        } catch (ExcepcionServicio e) {

            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/inicio";

        }

        modelo.put("comercio", comercio);

        return "perfilComercio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/modificarPerfil")
    public String modificar(HttpSession session, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        Comercio comercio = null;

        try {
            comercio = servicioComercio.buscarPorId(userMail);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }

        modelo.put("nombre", comercio.getNombre());
        modelo.put("apellido", comercio.getApellido());
        modelo.put("nombreComercio", comercio.getNombreComercio());
        modelo.put("direccion", comercio.getDireccion());
        modelo.put("telefono", comercio.getTelefono());
        modelo.put("rubros", Rubro.values());
        return "editarPerfilComercio.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/modificarPerfil")
    public String modificarComercio(HttpSession session, ModelMap modelo, @RequestParam String direccion, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String telefono, @RequestParam(required = false) String rubros, @RequestParam String nombreComercio,
             RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            servicioComercio.modificar(userMail, nombreComercio, nombre, apellido, telefono, direccion, rubros);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "editarPerfilComercio.html";
        }

        redirect.addFlashAttribute("exito", "Perfil modificado correctamente!!");

        return "redirect:/comercio/mostrarPerfil";

    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/validar")
    public String validar(ModelMap modelo, HttpSession session) {
        return "validar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/validarCupon")
    public String validarCupon(ModelMap modelo, HttpSession session, @RequestParam String idCupon, RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            servicioCupon.validarCupon(userMail, idCupon);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "validar.html";
        }

        redirect.addFlashAttribute("exito", "Cupón canjeado con exito");
        return "redirect:/comercio/validar";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/buscar")
    public String gestion(ModelMap modelo, @RequestParam(required = false) String mensaje) {
        if (mensaje != null) {
            modelo.put("mensaje", mensaje);
        }
        return "buscar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/buscarClientes")
    public String Clientes(@RequestParam String mailCliente, ModelMap modelo) {

        List<Cliente> clientes = servicioCliente.buscarClientes(mailCliente);

        modelo.put("clientes", clientes);

        return "buscar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/darPuntos")
    public String darPuntos(ModelMap modelo, @RequestParam String mail, @RequestParam Integer cantidad) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            servicioComercio.darPuntos(cantidad, mail, userMail);
            modelo.put("mensaje", "Se ha podido cargar los puntos correctamente.");
            return "buscar.html";

        } catch (ExcepcionServicio e) {
            modelo.put("mensaje", e.getMessage());

            return "buscar.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/cupones")
    public String verCupones(ModelMap modelo, HttpSession session) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        List<Cupon> cupones = servicioCupon.mostrarPorComercio(userMail);
        List<Integer> dados = new ArrayList();
        List<Integer> noDados = new ArrayList();
        for (Cupon cupon : cupones) {
            cupon.setId(cupon.getId().substring(24));
            
           
        }
        modelo.put("cupones", cupones);

        return "cuponeraComercio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/borrar")
    public String borrarCupones(ModelMap modelo, HttpSession session, @RequestParam String titulo, RedirectAttributes redirect) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        
        try {
            servicioCupon.borrar(titulo, userMail);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
        
        redirect.addFlashAttribute("exito", "Los cupones han sido borrados con exito");
        return "redirect:/comercio/cupones";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/detalleCupones")
    public String mostarDetalleCupones(ModelMap modelo, HttpSession session, @RequestParam String titulo){
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        
        List<Cupon> cupones = servicioCupon.buscarPorTituloyComercio(titulo, userMail);
        
         for (Cupon cupon : cupones) {
            cupon.setId(cupon.getId().substring(24));
            System.out.println(cupon.getTitulo());
        }
        
        modelo.put("cupones", cupones);
        return "detalleCuponesComercio.html";
    }

    @GetMapping("/cargar/{id}")
    public ResponseEntity<byte[]> cargarfoto(@PathVariable String id) {
        Foto foto = servicioFoto.buscarFoto(id);
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(foto.getMime())));
        return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
    }

}
