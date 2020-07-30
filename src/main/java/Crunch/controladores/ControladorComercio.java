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
            /**
             * FALTARIA hacer el html de CuponDeCanje que en teoría sería 
             * igual al cupon pero con el agregado de un input llamado
             * costo, para que me manda el costo en puntos de ese cupon.
             */
        return "cuponDeCanje.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/crearCuponDeCanje")
    public String crearCuponDeCanje(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam Integer costo, @RequestParam Integer cantidad, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        try {

            servicioCupon.crearCuponCanje(titulo, descripcion, vencimiento, userMail,costo,cantidad);

        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("vencimiento", vencimiento);
            modelo.put("costo",costo);
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

//    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
//    @PostMapping("/buscarClientes")
//    public String Clientes(@RequestParam String mailCliente, ModelMap modelo) {
//
//        List<Cliente> clientes = servicioCliente.buscarClientes(mailCliente);
//
//        modelo.put("clientes", clientes);
//
//        return "//TabladeClientes";
//    }

//    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
//    @PostMapping("/buscarClientes")
//    public String darPuntos(@RequestParam String mailCliente, ModelMap modelo) {
//        Cliente cliente = null;
//        try {
//            cliente = servicioCliente.buscarPorId(mailCliente);
//            modelo.put("cliente", cliente);
//        } catch (ExcepcionServicio e) {
//            modelo.put("error", e.getMessage());
//            return "error.html";
//        }
//        return "//darPuntos";
//
//    }
}
