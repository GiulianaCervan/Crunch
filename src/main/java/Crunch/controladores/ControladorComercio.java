/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioComercio;
import Crunch.servicios.ServicioCupon;
import Crunch.utilidades.Rubro;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/comercio")
public class ControladorComercio {

    @Autowired
    private ServicioComercio servicioComercio;
    @Autowired
    private ServicioCupon servicioCupon;
    @Autowired
    private ServicioCliente servicioCliente;

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
    public String crearCupon(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam Integer cantidad, ModelMap modelo) {

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

        modelo.put("exito", "Cupon creado correctamente");
        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/cuponDeCanje")
    public String cuponDeCanje() {

        return "cuponPuntos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/crearCuponDeCanje")
    public String crearCuponDeCanje(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam Integer costo, @RequestParam Integer cantidad, ModelMap modelo) {

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

            return "cuponDeCanje.html";
        } catch (Exception e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "error.html";
        }

        modelo.put("exito", "Cupon creado correctamente");
        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/mostrarPerfil")
    public String mostrarPerfilComercio(HttpSession session, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        Comercio comercio = null;

        try {

            comercio = servicioComercio.buscarPorId(userMail);
        } catch (ExcepcionServicio e) {

            modelo.put("error", e.getMessage());

            return "redirect:/inicio";

        }

        modelo.put("comercio", comercio);

        return "perfilComercio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/modificar")
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
            @RequestParam String apellido, @RequestParam String telefono, @RequestParam(required = false) String rubros, @RequestParam String nombreComercio) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            servicioComercio.modificar(userMail, nombreComercio, nombre, apellido, telefono, direccion, rubros);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "editarPerfilComercio.html";
        }

        modelo.put("exito", "Perfil modificado correctamente");

        return "redirect:/inicio";

    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/validar")
    public String validar(ModelMap modelo, HttpSession session) {
        return "validar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/validarCupon")
    public String validarCupon(ModelMap modelo, HttpSession session, @RequestParam String idCupon) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            servicioCupon.validarCupon(userMail, idCupon);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "validar.html";
        }

        modelo.put("exito", "Cupon canjeado con exito");
        return "validar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/buscar")
    public String gestion() {

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
    public String darPuntos(@RequestParam String mailCliente, ModelMap modelo, @RequestParam Integer cantidad, @RequestParam String idCupon) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {

            servicioComercio.darPuntos(cantidad, mailCliente, userMail);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
        return "buscar.html";

    }

}
