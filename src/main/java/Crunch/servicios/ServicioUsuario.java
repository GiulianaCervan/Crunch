/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.servicios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Comercio;
import Crunch.entidades.Usuario;
import Crunch.repositorios.ClienteRepositorio;
import Crunch.repositorios.ComercioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author lauta
 */
@Service
public class ServicioUsuario implements UserDetailsService {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ComercioRepositorio comercioRepositorio;
    
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        
       
        
        Optional<Comercio> respuestaComercio = comercioRepositorio.findById(mail);
        Optional<Cliente> respuestaCliente = clienteRepositorio.findById(mail);

        
       
        
        if (respuestaComercio.isPresent()) {
            
            Comercio usuarioComercio = respuestaComercio.get();

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_COMERCIO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuarioComercio);

            User user = new User(usuarioComercio.getMail(), usuarioComercio.getClave(), permisos);
            return user;

        } else if (respuestaCliente.isPresent()) {
            
            Cliente usuarioCliente = respuestaCliente.get();
            
            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_CLIENTE");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuarioCliente);

            User user = new User(usuarioCliente.getMail(), usuarioCliente.getClave(), permisos);
            return user;

        } else {
            throw new UsernameNotFoundException("El usuario no se encontro");
            
        }
        
        
        
        
//
    }


}
