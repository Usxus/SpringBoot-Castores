package com.castores.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 50, unique = true)
    private String correo;

    @Column(name = "contraseña", nullable = false, length = 100)
    private String contraseña;

    @Column(name = "idRol", nullable = false)
    private int idRol;

    @Column(name = "estatus", nullable = false)
    private int estatus;
}
