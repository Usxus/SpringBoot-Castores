package com.castores.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private int idProducto;

    @Column(nullable = false, length = 40)
    private String nombre;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int cantidad;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL)
    private EstadoProducto estadoProducto; // Relación con EstadoProducto
}
