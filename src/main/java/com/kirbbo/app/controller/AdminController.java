package com.kirbbo.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.kirbbo.app.model.Empleado;
import com.kirbbo.app.model.Pedido;
import com.kirbbo.app.model.Producto;
import com.kirbbo.app.service.EmpleadoService;
import com.kirbbo.app.service.PedidoService;
import com.kirbbo.app.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	@Autowired
	private ProductoService productoService;
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private EmpleadoService empleadoService;

	@GetMapping({ "/admin" })
	public String home(Model model, HttpSession session) {
		Empleado e = (Empleado) session.getAttribute("empleado");
		if (e == null) {
			return "redirect:/admin-login";
		}
		return "admin";
	}

	@GetMapping({ "/admin-inventario" })
	public String adminInventario(Model model, HttpSession session) {
		List<Producto> listaProductos = productoService.obtenerTodosLosProductos();
		model.addAttribute("listaProductos", listaProductos);
		return "admin-inventario";
	}

	@GetMapping({ "/admin-editar" })
	public String producto(@RequestParam(required = true) Integer producto, Model model) {
		Producto p = productoService.obtenerProductoPorId(producto);
		model.addAttribute("p", p);
		return "admin-editar";
	}

	@PostMapping({ "/admin-editar" })
	public String productoEditar(Model model, @ModelAttribute Producto producto,
			RedirectAttributes redirectAttributes) {
		int resultado = productoService.actualizar(producto);
		if (resultado == 0) {
			redirectAttributes.addFlashAttribute("mensaje", "Error actualizando producto");
			return "redirect:/admin-editar?" + "producto=" + producto.getIdProducto();
		}
		redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado correctamente :)");
		return "redirect:/admin-editar?" + "producto=" + producto.getIdProducto();
	}

	@GetMapping({ "/repartidor" })
	public String homeRepartidor(Model model, HttpSession session) {
		Empleado e = (Empleado) session.getAttribute("empleado");
		if (e == null) {
			return "redirect:/admin-login";
		}
		return "repartidor-home";
	}

	@GetMapping({ "/admin-login" })
	public String login(Model model) {
		Empleado empleado = new Empleado();
		model.addAttribute("empleado", empleado);
		return "admin-login";
	}

	@PostMapping({ "/admin-login", })
	public String loginPost(Model model, @ModelAttribute Empleado empleado, RedirectAttributes redirectAttributes,
			HttpSession session) {
		try {
			Empleado e = empleadoService.autenticarEmpleado(empleado.getUsernameEmpleado(),
					empleado.getPasswordEmpleado());
			session.setAttribute("empleado", e);
			if (e.getRol().getIdRol() == 1) {
				return "redirect:/admin";
			}
			return "redirect:/repartidor";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("mensaje", "Usuario o contraseña incorrectos");
			return "redirect:/admin-login";
		}

	}

	@PostMapping({ "/admin-logout", })
	public String logout(Model model, @ModelAttribute Empleado empleado, RedirectAttributes redirectAttributes,
			HttpSession session) {
		session.removeAttribute("empleado");
		return "redirect:/admin-login";

	}

	@PostMapping({ "/asignar", })
	public String asignarPedido(Model model, @RequestParam String id_pedido, HttpSession session) {
		Empleado e = (Empleado) session.getAttribute("empleado");
		int resultado = pedidoService.asignarPedido(id_pedido, e);
		if (resultado == 0) {
			return "redirect:/repartidor";
		}
		return "redirect:/repartidor-asignados";
	}

	@GetMapping({ "/repartidor-pedidos" })
	public String repartidorPedidos(Model model) {
		List<Pedido> pedidosEnProceso = pedidoService.obtenerPedidosSinAsignar();
		model.addAttribute("pedidosEnProceso", pedidosEnProceso);
		return "repartidor-pedidos";
	}

	@GetMapping({ "/repartidor-asignados" })
	public String repartidorAsignados(Model model, HttpSession session) {
		Empleado e = (Empleado) session.getAttribute("empleado");
		List<Pedido> pedidosAsignados = pedidoService.obtenerPedidosPorEmpleado(e.getIdEmpleado());
		model.addAttribute("pedidosAsignados", pedidosAsignados);
		return "repartidor-asignados";
	}

	@PostMapping({ "/finalizar", })
	public String finalizarPedido(Model model, @RequestParam String id_pedido, HttpSession session) {
		int resultado = pedidoService.finalizarPedido(id_pedido);
		if (resultado == 0) {
			return "redirect:/repartidor";
		}
		return "redirect:/repartidor-asignados";
	}
}
