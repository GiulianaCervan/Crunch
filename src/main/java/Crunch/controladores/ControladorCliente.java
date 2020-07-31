/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Cupon;
import Crunch.entidades.Puntos;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioCupon;
import java.util.ArrayList;
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

/**
 *
 * @author Duilio Di Tommasi
 */
@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ServicioCupon servicioCupon;
    @Autowired
    private ServicioCliente servicioCliente;


    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @PostMapping("/otorgar")
    public String otorgarCupon(@RequestParam String titulo, @RequestParam String mailComercio, ModelMap modelo) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;

            String userMail = userDetails.getUsername();
            String idCupon = servicioCupon.buscarCuponDisponible(titulo, mailComercio);
            Cupon cupon = servicioCupon.buscarCuponPorId(idCupon);
            
            if (cupon.getCosto() == null){
                servicioCupon.otorgar(userMail, idCupon);
            }else if(cupon.getCosto() != null){
                servicioCupon.otorgarCuponCanje(userMail, idCupon);
            }
            
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "redirect:/inicio";
        }

        return "exitoCuponAdq.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/cupones")
    public String mostrarMisCupones(ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        List<Cupon> cupones = servicioCupon.mostrarCuponesCliente(userMail);
        modelo.put("cupones", cupones);

        return "cuponera.html";
    }


    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        Cliente cliente = null;
        try {
            cliente = servicioCliente.buscarPorId(userMail);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
        }

        
        modelo.put("nombre", cliente.getNombre());
        modelo.put("apellido", cliente.getApellido());
        modelo.put("telefono", cliente.getTelefono());
        modelo.put("domicilio", cliente.getDomicilio());
        
        return "perfilCliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/modificar")
    public String modificar(HttpSession session, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        Cliente cliente = null;
        try {
            cliente = servicioCliente.buscarPorId(userMail);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "error.html";
        }
        modelo.put("nombre", cliente.getNombre());
        modelo.put("apellido", cliente.getApellido());
        modelo.put("telefono", cliente.getTelefono());
        modelo.put("domicilio", cliente.getDomicilio());

        return "editarPerfilUsuario.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @PostMapping("/modificarPerfil")
    public String modificarPerfil(HttpSession session, ModelMap modelo, @RequestParam String domicilio, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String telefono) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();

        try {
            
           servicioCliente.modificar(userMail, nombre, apellido, domicilio, telefono);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("domicilio", domicilio);

            return "editarPerfilUsuario.html";


        }
        modelo.put("exito", "Perfil modificado con exito");
        return "redirect:/inicio";

    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/mostrarPuntos")
    public String mostrarPuntos(HttpSession session, ModelMap modelo) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userMail = userDetails.getUsername();
        List<Puntos> puntos = new ArrayList();
        try {
            puntos = servicioCliente.mostrarPuntos(userMail);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        
        modelo.put("puntos", puntos);
        
        return "//mostarPuntos";
      
    }

}
