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


@Controller
@RequestMapping("/comercio")
public class ControladorComercio {
    
    @Autowired
    private ServicioComercio servicioComercio;
    @Autowired
    private ServicioCupon servicioCupon;
    
    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
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
   
    @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @GetMapping("/cupon")
    public String cupon(){
        return "cupon.html";
    }
   
   @PreAuthorize("hasAnyRole('ROLE_COMERCIO')")
    @PostMapping("/crearCupon")
    public String crearCupon(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String vencimiento, @RequestParam  Integer cantidad ,ModelMap modelo){
        
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
        }catch (Exception e){
            e.printStackTrace();
            modelo.put("error", e.getMessage());
            return "error.html";
        }
       
        modelo.put("exito", "Cupon creado correctamente");
        return "redirect:/inicio";
    }
}
