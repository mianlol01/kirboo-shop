package com.kirbbo.app.controller;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import javax.sql.DataSource;

@Controller
public class ReportController {

	@Autowired
	private DataSource dataSource; // javax.sql

	@Autowired
	private ResourceLoader resourceLoader; // core.io

	@GetMapping("/reportes-productos")
	public void reportesProductos(HttpServletResponse response) {
		// opción 1
		response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
		// opción 2
		response.setHeader("Content-Disposition", "inline;");

		response.setContentType("application/pdf");
		try {
			String ru = resourceLoader.getResource("classpath:static/productos.jasper").getURI().getPath();
			JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
			OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/reportes-categorias")
	public void reportesCategorias(HttpServletResponse response) {
		// opción 1
		response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
		// opción 2
		response.setHeader("Content-Disposition", "inline;");

		response.setContentType("application/pdf");
		try {
			String ru = resourceLoader.getResource("classpath:static/categoriasventas.jasper").getURI().getPath();
			JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
			OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@GetMapping("/reporte")
	public void reporte(HttpServletResponse response) {
		// opción 1
		response.setHeader("Content-Disposition", "attachment; filename=\"reporte.pdf\";");
		// opción 2
		response.setHeader("Content-Disposition", "inline;");

		response.setContentType("application/pdf");
		try {
			String ru = resourceLoader.getResource("classpath:static/reporte.jasper").getURI().getPath();
			JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
			OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}