
package Crunch.repositorios;

import Crunch.entidades.Cupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CuponRepositorio extends JpaRepository<Cupon, String> {
   
}
