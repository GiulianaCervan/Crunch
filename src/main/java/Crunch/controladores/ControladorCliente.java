/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import Crunch.entidades.Cliente;
import Crunch.entidades.Cupon;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCliente;
import Crunch.servicios.ServicioCupon;
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
            servicioCupon.otorgar(userMail, idCupon);
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
    public String perfil(){
       
      return "perfilCliente.html";  
    }
}
