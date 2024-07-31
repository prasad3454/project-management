package com.proj.controller;



import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.lowagie.text.DocumentException;
import com.proj.entities.Email;
import com.proj.entities.Project;
import com.proj.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3001")
@Controller
//@RequestMapping("/api/demo")
public class ProjectController {
	
	@Autowired
	private ProjectService service;
	
	@GetMapping("/projects")
	public ResponseEntity<List<Project>> getProject() {
	    List<Project> proj = service.getAllProject();
	    return new ResponseEntity<>(proj, HttpStatus.OK);
	}
	
	@PostMapping("/addProject")
	public ResponseEntity<?> addProject( @RequestBody Project project,BindingResult result){
		try {
		if(result.hasErrors()) {
			
			return new ResponseEntity<>("Validation Errors:"+result.getAllErrors(),HttpStatus.BAD_REQUEST);
		}
		Project proj=service.addProject(project);
		return new ResponseEntity<>(proj,HttpStatus.CREATED);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error adding project"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/delete/{pid}")
	public ResponseEntity<?> deleteProject(@PathVariable Long pid){
		service.deleteProject(pid);
		return new ResponseEntity<>(pid, HttpStatus.OK);
	}
	
	@GetMapping("/edit/{pid}")
	public ResponseEntity<?> editProject(@PathVariable Long pid){
		Project project=service.getProjectById(pid);
		if(project != null) {
			return new ResponseEntity<>(project,HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Project id not found",HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/update/{pid}")
	public ResponseEntity<?> editProject(@PathVariable Long pid,@RequestBody Project project){
		Project project1 = service.getProjectById(pid);
	    
	    if(project1 != null) {
	        // Set the project ID to ensure it matches the path variable
	        project.setPid(pid);
	        
	        // Perform the update
	        service.addProject(project);

	        return new ResponseEntity<>(project, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Project id not found", HttpStatus.NOT_FOUND);
	    }
	}
	
	@PostMapping("/sendMail")
	public ResponseEntity<?> sendMail(@RequestPart("to") String to,
            @RequestPart("subject") String subject,
            @RequestPart("message") String message,
            @RequestPart(value = "attachFile", required = false) MultipartFile attachFile) {
		try {
		Email email=new Email();
		email.setTo(to);
		email.setSubject(subject);
		email.setMessage(message);
		service.sendMail(email,attachFile);
		return new ResponseEntity<>(email,HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}
	
	// In your Spring Boot controller
	@GetMapping("/exportPdf")
	public ResponseEntity<byte[]> generatePdfFile(HttpServletResponse response) throws DocumentException, IOException {
	    List<Project> listofprojects = service.getAllProject();
	    byte[] pdfBytes = service.generatePdf(listofprojects, response);
	    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
	}

	
	@GetMapping("/exportExcel")
	public ResponseEntity<byte[]> exportToExcel(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "application;filename=projects.xls";
		response.setHeader(headerKey, headerValue);
		
		byte[] excelBytes = service.generateExcel();
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelBytes);
	}
	
	@GetMapping("/exportCsv")
	public void exportToCsv(HttpServletResponse response) throws Exception {
		service.generateCsv(response);
	}
}
