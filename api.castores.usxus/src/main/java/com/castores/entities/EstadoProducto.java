package com.castores.entities;

import com.castores.entities.Producto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "estado_producto")
public class EstadoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoProducto")
    private Integer idEstadoProducto;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto", nullable = false)
    private Producto producto;
}