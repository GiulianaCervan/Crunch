
package Crunch.repositorios;

import Crunch.entidades.Cliente;
import Crunch.entidades.Cupon;
import Crunch.entidades.CuponDeCanje;
import Crunch.entidades.Raspadita;
import Crunch.entidades.Valoracion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente,String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.cuponPromo.id = :id")
    public List<Cupon>cuponPromo(@Param("id") String id);
   
     @Query("SELECT c FROM Cliente c WHERE c.cuponCanje.id = :id")
    public List<CuponDeCanje>cuponCanje(@Param("id") String id);
    
     @Query("SELECT c FROM Cliente c WHERE c.raspaditas.id = :id")
     public List<Raspadita>raspaditas(@Param("id") String id);
     
      @Query("SELECT c FROM Cliente c WHERE c.valoraciones.id = :id")
      public List<Valoracion>valoraciones(@Param("id") String id);
}
