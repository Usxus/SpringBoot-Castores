package com.castores.repository;

import com.castores.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    @Query("SELECT SUM(v.cantidad) FROM Venta v WHERE v.producto.idProducto = :idProducto")
    Integer sumarVentasPorProducto(int idProducto);
    
}
