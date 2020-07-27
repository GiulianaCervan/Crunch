/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Duilio Di Tommasi
 */
@Controller
@RequestMapping("/cliente")
public class ControladorCliente {
    
    @PostMapping("/otorgar/{id}")
    public void otorgarCupon(@PathVariable String id){
        
        
    }
}
