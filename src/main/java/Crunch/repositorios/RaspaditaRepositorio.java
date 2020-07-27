package Crunch.repositorios;

import Crunch.entidades.Raspadita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaspaditaRepositorio extends JpaRepository<Raspadita,String>{
    
}
