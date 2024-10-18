package com.castores.controller;

import com.castores.entities.EstadoProducto;
import com.castores.entities.HistoricoMovimiento;
import com.castores.entities.Producto;
import com.castores.exceptions.CustomExceptions.BadRequestException;
import com.castores.exceptions.CustomExceptions.ForbiddenException;
import com.castores.exceptions.CustomExceptions.ResourceNotFoundException;
import com.castores.repository.EstadoProductoRepository;
import com.castores.repository.HistoricoMovimientoRepository;
import com.castores.repository.ProductoRepository;
import com.castores.util.Constantes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/almacen")
public class InventarioController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoProductoRepository estadoProductoRepository;

    @Autowired
    private HistoricoMovimientoRepository historicoMovimientoRepository;

    /**
     * Ver el inventario de productos.
     */
    @GetMapping("/inventario")
    public String verInventario(@RequestParam(required = false) Boolean activo, Model model) {
        try {
            List<Producto> productos;
            if (activo == null) {
                productos = productoRepository.findAll();
            } else {
                productos = productoRepository.findProductosByEstado(activo);
            }
            Map<Integer, Integer> cantidades = new HashMap<>();
            productos.forEach(producto -> cantidades.put(producto.getIdProducto(), producto.getCantidad()));
            model.addAttribute("productos", productos);
            model.addAttribute("cantidades", cantidades);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            boolean esAlmacenista = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ALMACENISTA"));
            model.addAttribute("esAdmin", esAdmin);
            model.addAttribute("esAlmacenista", esAlmacenista);
            if (esAdmin) {
                List<HistoricoMovimiento> movimientos = historicoMovimientoRepository.findAll();
                model.addAttribute("movimientos", movimientos);
            } else {
                model.addAttribute("movimientos", null);
            }
            return "inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "inventario";
        }
    }

    /**
     * Agregar un nuevo producto al inventario.
     */
    @PostMapping("/producto/agregar")
    public String agregarProducto(@RequestParam String nombre, @RequestParam BigDecimal precio, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCantidad(0);
            productoRepository.save(producto);
            EstadoProducto estadoProducto = new EstadoProducto();
            estadoProducto.setProducto(producto);
            estadoProducto.setActivo(true);
            estadoProductoRepository.save(estadoProducto);
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(producto.getIdProducto());
            movimiento.setCantidad(0);
            movimiento.setTipoMovimiento("creación de producto");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Modificar la cantidad de un producto en el inventario.
     */
    @PostMapping("/producto/modificarCantidad")
    public String modificarCantidadProducto(@RequestParam int idProducto, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();

            Producto producto = productoRepository.findById(idProducto).orElse(null);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("error", Constantes.CANTIDAD_REGISTRO);
                throw new ResourceNotFoundException(Constantes.CANTIDAD_REGISTRO);
            }
            EstadoProducto estadoProducto = estadoProductoRepository.findByIdProducto(idProducto);
            if (estadoProducto == null || !estadoProducto.getActivo()) {
                redirectAttributes.addFlashAttribute("error", Constantes.CANTIDAD_INCTIVO);
                throw new ForbiddenException(Constantes.CANTIDAD_INCTIVO);
            }
            if (cantidad <= 0) {
                redirectAttributes.addFlashAttribute("error", Constantes.CANTIDAD_MAX);
                throw new BadRequestException(Constantes.CANTIDAD_MAX);
            }
            producto.setCantidad(cantidad);
            productoRepository.save(producto);
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(cantidad);
            movimiento.setTipoMovimiento("modificación de cantidad de inventario");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);

            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Restar una cantidad de producto del inventario.
     */
    @PostMapping("/producto/restarCantidad")
    public String restarCantidadProducto(@RequestParam int idProducto, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();

            Producto producto = productoRepository.findById(idProducto).orElse(null);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("error", Constantes.RESTA_REGISTRO);
                throw new ResourceNotFoundException(Constantes.RESTA_REGISTRO);
            }
            EstadoProducto estadoProducto = estadoProductoRepository.findByIdProducto(idProducto);
            if (estadoProducto == null || !estadoProducto.getActivo()) {
                redirectAttributes.addFlashAttribute("error", Constantes.RESTA_INCTIVO);
                throw new ForbiddenException(Constantes.RESTA_INCTIVO);
            }
            if (cantidad <= 0) {
                redirectAttributes.addFlashAttribute("error", Constantes.CANTIDAD_MAX);
                throw new BadRequestException(Constantes.CANTIDAD_MAX);
            }
            int cantidadRestante = producto.getCantidad() - cantidad;
            if (cantidadRestante < 0) {
                redirectAttributes.addFlashAttribute("error", Constantes.RESTA_NEGATIVO);
                return "redirect:/almacen/inventario";
            }
            producto.setCantidad(cantidadRestante);
            productoRepository.save(producto);

            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(-cantidad);
            movimiento.setTipoMovimiento("salida");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Dar de baja un producto (desactivar).
     */
    @PostMapping("/producto/baja")
    public String darBajaProducto(@RequestParam int idProducto, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();

            EstadoProducto estadoProducto = estadoProductoRepository.findByIdProducto(idProducto);
            if (estadoProducto != null) {
                estadoProducto.setActivo(false);
                estadoProductoRepository.save(estadoProducto);
            }
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(0);
            movimiento.setTipoMovimiento("inactivado");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Activar un producto dado de baja.
     */
    @PostMapping("/producto/activar")
    public String activarProducto(@RequestParam int idProducto, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();
            EstadoProducto estadoProducto = estadoProductoRepository.findByIdProducto(idProducto);
            if (estadoProducto != null) {
                estadoProducto.setActivo(true);
                estadoProductoRepository.save(estadoProducto);
            }
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(0);
            movimiento.setTipoMovimiento("activado");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Registrar una entrada de productos en el inventario.
     */
    @PostMapping("/producto/entrada")
    public String registrarEntradaProducto(@RequestParam int idProducto, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();
            Producto producto = productoRepository.findById(idProducto).orElse(null);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
                return "redirect:/almacen/inventario";
            }
            producto.setCantidad(producto.getCantidad() + cantidad);
            productoRepository.save(producto);
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(cantidad);
            movimiento.setTipoMovimiento("entrada");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            redirectAttributes.addFlashAttribute("error", "Error al registrar entrada: " + e.getMessage());
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Registrar una salida de productos en el inventario.
     */
    @PostMapping("/producto/salida")
    public String registrarSalidaProducto(@RequestParam int idProducto, @RequestParam int cantidad, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String usuario = authentication.getName();
            Producto producto = productoRepository.findById(idProducto).orElse(null);
            if (producto == null) {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
                return "redirect:/almacen/inventario";
            }
            int cantidadRestante = producto.getCantidad() - cantidad;
            if (cantidadRestante < 0) {
                redirectAttributes.addFlashAttribute("error", "No hay suficiente cantidad en inventario para realizar esta operación.");
                return "redirect:/almacen/inventario";
            }
            producto.setCantidad(cantidadRestante);
            productoRepository.save(producto);
            HistoricoMovimiento movimiento = new HistoricoMovimiento();
            movimiento.setIdProducto(idProducto);
            movimiento.setCantidad(-cantidad);
            movimiento.setTipoMovimiento("salida");
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setUsuario(usuario);
            historicoMovimientoRepository.save(movimiento);
            return "redirect:/almacen/inventario";
        } catch (Exception e) {
            LOGGER.error(e);
            redirectAttributes.addFlashAttribute("error", "Error al registrar salida: " + e.getMessage());
            return "redirect:/almacen/inventario";
        }
    }

    /**
     * Cerrar sesión.
     */
    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
