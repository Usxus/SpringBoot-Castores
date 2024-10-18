package com.castores.repository;

import com.castores.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT p FROM Producto p JOIN EstadoProducto ep ON p.idProducto = ep.producto.idProducto WHERE ep.activo = :activo")
    List<Producto> findProductosByEstado(boolean activo);

}
