package com.castores.repository;

import com.castores.entities.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoProductoRepository extends JpaRepository<EstadoProducto, Integer> {

    @Query("SELECT ep FROM EstadoProducto ep WHERE ep.producto.idProducto = :idProducto")
    EstadoProducto findByIdProducto(int idProducto);
    
}
