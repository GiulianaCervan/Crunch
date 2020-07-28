/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Crunch.repositorios;

import Crunch.entidades.Puntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Duilio Di Tommasi
 */
@Repository
public interface PuntosRepositorio extends JpaRepository<Puntos, String> {
    
}
