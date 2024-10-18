package com.kirbbo.app.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.kirbbo.app.model.Pedido;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class SessionControllerAdvice {

	@ModelAttribute
	public void initCarrito(HttpSession session) {
		Pedido carrito = new Pedido();
		if (session.getAttribute("carrito") == null) {
			session.setAttribute("carrito", carrito);
			System.out.println(carrito.calcularArticulos());
		}
	}
}