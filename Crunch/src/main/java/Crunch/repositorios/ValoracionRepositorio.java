
package Crunch.repositorios;

import Crunch.entidades.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepositorio extends JpaRepository<Valoracion, String> {
    
}
