
package Crunch.servicios;

import Crunch.entidades.Foto;
import Crunch.repositorios.FotoRepositorio;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author JULIETA
 */

@Service
public class ServicioFoto {
//    @Autowired
//    private FotoRepositorio FotoRepositorio;
//
//
//    public Foto guardar(MultipartFile archivo)throws ErrorService, IOException{
//        if (archivo !=null){
//
//          try{
//            Foto foto = new Foto();
//            foto.setMime(archivo.getContentType());
//            foto.setNombre(archivo.getName());
//            foto.setContedido(archivo.getBytes());
//            return FotoRepositorio.save(foto);  
//        } catch(Exception e){
//                System.err.println(e.getMessage());
//       }
//    }else{
//            return null;
//        }
//        public Foto actualizar (String idFoto, MultipartFile archivo) throws ErrorService{
//    
//if (archivo !=null){
//
//          try{
//            Foto foto = new Foto();
//            
//            if(idFoto !=null){
//                Optional<foto> respuesta = fotoRepositorio.findById(idFoto);
//                if(respuesta.isPresent()){
//                    foto = respuesta.get();
//                }
//            }
//            foto.setMime(archivo.getContentType());
//            foto.setNombre(archivo.getName());
//            foto.setContedido(archivo.getBytes());
//            return FotoRepositorio.save(foto);  
//        } catch(Exception e){
//                System.err.println(e.getMessage());
//        }
//          return null;
}



