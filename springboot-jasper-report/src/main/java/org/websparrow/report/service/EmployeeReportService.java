package org.websparrow.report.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.websparrow.report.dto.Employee;
import org.websparrow.report.repository.EmployeeRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class EmployeeReportService {

	@Value("${report.path}")
	private String reportPath;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public String generateReport() {
		List<Employee> employees = new ArrayList<>();
		employeeRepository.findAll().stream().forEach(e -> employees.add(e));
		
		try {
			File file = ResourceUtils.getFile("classpath:employee-rpt.jrxml");
			InputStream input = new FileInputStream(file);

			// Compile the Jasper report from .jrxml to .japser
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// Get your data source
			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(employees);

			// Add parameters
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("createdBy", "JavaHelper.org");

			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);

			// Export the report to a PDF file
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Empployee.pdf");
			System.out.println("PDF File Generated !!");

			JasperExportManager.exportReportToXmlFile(jasperPrint, reportPath + "\\Employee.xml", true);
			System.out.println("XML File Generated !!");
			
			JasperExportManager.exportReportToHtmlFile(jasperPrint, reportPath + "\\Employee.html");

			return "Report successfully generated @path= " + reportPath;

		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
