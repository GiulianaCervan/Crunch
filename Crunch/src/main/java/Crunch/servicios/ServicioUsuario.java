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
        
           Usuario usuario = null;
           if (usuario == null){
               usuario = clienteRepositorio.getOne(mail);
           }
           if(usuario == null){
               usuario = comercioRepositorio.getOne(mail);
           }
           
        if (usuario != null && usuario.getClass().getName().equals(Cliente.class.getName())) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_CLIENTE");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else if (usuario != null && usuario.getClass().getName().equals(Comercio.class.getName())){
            

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_COMERCIO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        }else {
            return null;
        }
    }


}