package com.kirbbo.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kirbbo.app.model.Categoria;
import com.kirbbo.app.model.Cliente;
import com.kirbbo.app.model.DetallePedido;
import com.kirbbo.app.model.Pedido;
import com.kirbbo.app.model.Producto;
import com.kirbbo.app.service.CarritoService;
import com.kirbbo.app.service.CategoriaService;
import com.kirbbo.app.service.ClienteService;
import com.kirbbo.app.service.PedidoService;
import com.kirbbo.app.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {

	@Autowired
	private ProductoService productoService;
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private CarritoService carritoService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PedidoService pedidoService;

	@GetMapping({ "/", "/home", "/inicio", "/index" })
	public String home(Model model) {
		List<Producto> listaDestacados = productoService.obtenerProductosDestacados();
		model.addAttribute("listaDestacados", listaDestacados);
		return "index";
	}

	@GetMapping({ "/shop", "/menu", "/store" })
	public String menu(@RequestParam(required = false) Integer categoria, Model model) {
		List<Producto> listaProductos = new ArrayList<Producto>();
		if (categoria != null) {
			Categoria c = categoriaService.obtenerCategoria(categoria);
			model.addAttribute("nombreCategoria", c.getNombreCategoria().toUpperCase());
			listaProductos = productoService.obtenerProductosPorCategoria(categoria);
		} else {
			model.addAttribute("nombreCategoria", "TODOS");
			listaProductos = productoService.obtenerTodosLosProductos();
		}
		List<Categoria> listaCategorias = categoriaService.obtenerCategorias();
		model.addAttribute("listaProductos", listaProductos);
		model.addAttribute("listaCategorias", listaCategorias);

		return "menu";
	}

	@GetMapping({ "/detalles" })
	public String producto(@RequestParam(required = true) Integer producto, Model model) {
		Producto p = productoService.obtenerProductoPorId(producto);
		List<Producto> listaSimilares = productoService.obtenerProductosPorCategoria(p.getIdCategoria());
		model.addAttribute("p", p);
		model.addAttribute("listaSimilares", listaSimilares);
		return "producto";
	}

	@GetMapping({ "/ordenar", "/carrito", "/cart" })
	public String ordenar(Model model) {
		List<Producto> listaDestacados = productoService.obtenerProductosDestacados();
		model.addAttribute("listaDestacados", listaDestacados);
		return "ordenar";
	}

	@PostMapping({ "/ordenar" })
	public String ordenarPost(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
		Cliente c = (Cliente) session.getAttribute("cliente");
		if (c == null) {
			return "redirect:/login";
		}

		return "redirect:/ordenar";
	}

	@PostMapping({ "/cart" })
	public String agregarCarrito(@RequestParam int id_producto, @RequestParam int cantidad, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Producto p = productoService.obtenerProductoPorId(id_producto);
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		int[] resultado = carritoService.agregarCarrito(carrito, id_producto, cantidad);
		if (resultado[0] == -1) {
			redirectAttributes.addFlashAttribute("mensaje",
					"No puedes añadir esa cantidad(" + cantidad + ") al carrito — tenemos " + p.getStock()
							+ " existencias y has añadido " + resultado[1] + " en tu carrito");
			return "redirect:/detalles?producto=" + id_producto;
		}
		session.setAttribute("carrito", carrito);
		return "redirect:/ordenar";
	}

	@PostMapping({ "/editar-carrito" })
	public String editarCarrito(@RequestParam int id_producto, @RequestParam int cantidad, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		for (DetallePedido detalleEditar : carrito.getDetalles()) {
			if (detalleEditar.getProducto().getIdProducto() == id_producto) {
				detalleEditar.setCantidad(cantidad);
			}
		}
		session.setAttribute("carrito", carrito);
		return "redirect:/ordenar";
	}

	@PostMapping({ "/eliminar-carrito" })
	public String eliminarCarrito(@RequestParam int id_producto, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		int resultado = carrito.eliminarCarrito(id_producto);
		if (resultado == 0) {
			System.out.println("error eliminando del carrito");
			return "redirect:/ordenar";
		}
		session.setAttribute("carrito", carrito);
		return "redirect:/ordenar";
	}

	@GetMapping({ "/login", })
	public String login(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "login";
	}

	@PostMapping({ "/login", })
	public String loginPost(Model model, @ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		try {
			Cliente c = clienteService.autenticarCliente(cliente.getUsernameCliente(), cliente.getPasswordCliente());
			session.setAttribute("cliente", c);
			session.setAttribute("address", c.getAddressCliente());
			carrito.setAddress(c.getAddressCliente());
			return "redirect:/";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("mensaje", "Usuario o contraseña incorrectos");
			return "redirect:/login";
		}

	}

	@GetMapping({ "/signup", })
	public String signup(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "signup";
	}

	@PostMapping({ "/signup", })
	public String signupPost(Model model, @ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		int resultado = clienteService.registrarCliente(cliente);
		if (resultado == 0) {
			redirectAttributes.addFlashAttribute("mensaje", "Usuario no disponible, por favor ingrese otro");
			return "redirect:/signup";
		}
		try {
			Cliente c = clienteService.autenticarCliente(cliente.getUsernameCliente(), cliente.getPasswordCliente());
			session.setAttribute("cliente", c);
			session.setAttribute("address", c.getAddressCliente());
			carrito.setAddress(c.getAddressCliente());
			return "redirect:/";
		} catch (RuntimeException e) {
			return "redirect:/signup";
		}

	}

	@GetMapping({ "/perfil", })
	public String perfil(Model model) {
		return "account";
	}

	@PostMapping({ "/editar-address", })
	public String editarAddress(Model model, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam String address) {
		Cliente c = (Cliente) session.getAttribute("cliente");
		clienteService.actualizarDireccion(c.getIdCliente(), address);
		Cliente clienteActualizado = clienteService.obtenerClientePorId(c.getIdCliente());
		session.setAttribute("cliente", clienteActualizado);
		redirectAttributes.addFlashAttribute("mensajeAddress", "Dirección actualizada correctamente :)");
		return "redirect:/perfil";
	}

	@PostMapping({ "/editar-telefono", })
	public String editarTelefono(Model model, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam String telefono) {
		Cliente c = (Cliente) session.getAttribute("cliente");
		clienteService.actualizarTelefono(c.getIdCliente(), telefono);
		Cliente clienteActualizado = clienteService.obtenerClientePorId(c.getIdCliente());
		session.setAttribute("cliente", clienteActualizado);
		redirectAttributes.addFlashAttribute("mensajeTelefono", "Teléfono actualizado correctamente :)");
		return "redirect:/perfil";
	}

	@PostMapping({ "/editarPedidoAddress", })
	public String editarPedidoAddress(Model model, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam String address) {
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		carrito.setAddress(address);
		session.setAttribute("carrito", carrito);
		redirectAttributes.addFlashAttribute("mensaje", "Dirección de envío cambiada correctamente :)");
		return "redirect:/ordenar";
	}

	@PostMapping({ "/reestablecerPedidoAddress", })
	public String reestablecerPedidoAddress(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
		System.out.println("ola bb");
		Cliente c = (Cliente) session.getAttribute("cliente");
		if (c == null) {
			redirectAttributes.addFlashAttribute("mensaje", "Debe iniciar sesión para acceder a esta función :)");
			return "redirect:/ordenar";
		}
		Pedido carrito = (Pedido) session.getAttribute("carrito");
		carrito.setAddress(c.getAddressCliente());
		session.setAttribute("carrito", carrito);
		redirectAttributes.addFlashAttribute("mensajeTelefono", "Dirección de envío cambiada correctamente :)");
		return "redirect:/ordenar";
	}

	@GetMapping({ "/pedido", })
	public String pedido(Model model, HttpSession session) {
		Cliente c = (Cliente) session.getAttribute("cliente");
		if (c == null) {
			return "redirect:/login";
		}
		List<Pedido> pedidosEnProceso = pedidoService.obtenerPedidosConEstado1(c.getIdCliente());
		List<Pedido> pedidosFinalizados = pedidoService.obtenerPedidosConEstado2(c.getIdCliente());
		model.addAttribute("pedidosEnProceso", pedidosEnProceso);
		model.addAttribute("pedidosFinalizados", pedidosFinalizados);
		System.out.println(pedidosEnProceso.size());
		return "pedido";
	}

	@PostMapping({ "/pedido" })
	public String pedidoPost(Model model, HttpSession session) {
	    // Obtener el cliente de la sesión
	    Cliente c = (Cliente) session.getAttribute("cliente");
	    if (c == null) {
	        return "redirect:/login";
	    }
	    Pedido carrito = (Pedido) session.getAttribute("carrito");
	    carrito.setCliente(c);
	    int resultado = pedidoService.generarPedido(carrito);
	    if (resultado == 0) {
	    	System.out.println("ayayay");
	        return "redirect:/ordenar";
	    }
	    session.removeAttribute("carrito");
	    Pedido carrito2 = new Pedido();
	    carrito2.setAddress(c.getAddressCliente());
	    session.setAttribute("carrito", carrito2);
	    
	    return "redirect:/pedido";
	}
	@PostMapping({ "/logout" })
	public String logout(Model model, HttpSession session) {
	    session.removeAttribute("cliente");    
	    return "redirect:/login";
	}
}