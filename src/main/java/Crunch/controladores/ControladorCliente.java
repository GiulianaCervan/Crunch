/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import Crunch.excepciones.ExcepcionServicio;
import Crunch.servicios.ServicioCupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
   
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @PostMapping("/otorgar/")
    public String otorgarCupon(@RequestParam String titulo, @RequestParam String mailComercio, ModelMap modelo){
        try {
            servicioCupon.buscarCuponDisponible(titulo, mailComercio);
        } catch (ExcepcionServicio e) {
            modelo.put("error", e.getMessage());
            return "redirect:/inicio";
        }
        
        return "";
        
    }
}
