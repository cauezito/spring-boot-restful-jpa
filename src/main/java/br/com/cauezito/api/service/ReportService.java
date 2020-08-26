package br.com.cauezito.api.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ReportService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public byte[] buildReport (String name, ServletContext servlet) {
		Connection connection = null;
		try {
			connection = jdbc.getDataSource().getConnection();
			String jasperPath = servlet.getRealPath("reports") + 
					File.separator + "users" + ".jasper";
			
			JasperPrint print = JasperFillManager.fillReport(jasperPath, new HashMap(), connection);
		
			return JasperExportManager.exportReportToPdf(print);
			
		} catch (SQLException | JRException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	
	}
}
