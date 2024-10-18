package com.castores.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "historico_movimiento")
public class HistoricoMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private int idMovimiento;

    @Column(name = "id_producto")
    private int idProducto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "tipo_movimiento")
    private String tipoMovimiento;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @Column(name = "usuario")
    private String usuario;
}
