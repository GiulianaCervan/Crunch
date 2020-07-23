
package Crunch.controladores;

import Crunch.servicios.ServicioCupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Cupon")
public class ControladorCupon {
    
    @Autowired
    private ServicioCupon servicioCupon;
    
    
    @GetMapping("")
    public void mostrarCupones(ModelMap modelo, String rubro){
        
        try {
            modelo.put("cupones", servicioCupon.mostrarPorRubros(rubro));
        } catch (Exception e) {
        }
        
    }
    
}
