/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import Crunch.entidades.Comercio;
import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioComercio;
import Crunch.servicios.ServicioCupon;
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
@RequestMapping("/comercio")
public class ControladorComercio {
    
    @Autowired
    private ServicioComercio servicioComercio;
    @Autowired
    private ServicioCupon servicioCupon;
    
    @PreAuthorize("hasAnyRol('ROLE_COMERCIO')")
    @GetMapping("/perfil")
    public String mostrarPerfil(ModelMap modelo, HttpSession session){
        
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
    
    @GetMapping("/cupon")
    public String cupon(){
        return "cupon.html";
    }
   // @PreAuthorize("hasAnyRol('ROLE_COMERCIO')")
    @PostMapping("/crearCupon")
    public String crearCupon(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento,ModelMap modelo){
        
        try {
            System.out.println(titulo);
            System.out.println(descripcion);
            System.out.println(vencimiento);
            
            
        } catch (Exception e) {
        }
        return "";
    }
}
