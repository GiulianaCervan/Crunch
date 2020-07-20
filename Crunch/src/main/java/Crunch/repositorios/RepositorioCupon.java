/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.repositorios;

import Crunch.entidades.Cupon;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author duili
 */

@Repository
public interface RepositorioCupon extends JpaRepository<Cupon, String>  {
    
    @Query("SELECT c FROM Cupon c WHERE c.titulo = :titulo AND c.mailComercio = :mailComercio")
    public List<Cupon> buscarPorTituloyComercio(@Param("titulo")String titulo,@Param("mailComercio")String mailComercio);

    
}
