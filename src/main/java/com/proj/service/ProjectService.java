package com.proj.service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proj.entities.Email;
import com.proj.entities.Project;
import com.proj.repository.ProjectRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;

@Service()
public class ProjectService {
	
	@Autowired
	private ProjectRepository repo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	//Add new project
	public Project addProject(Project project) {
		return repo.save(project);
	}
	
	//Get All Details
	public List<Project> getAllProject() {
	    List<Project> projects = repo.findAll();
	    System.out.println("Number of projects fetched from the database: " + projects.size());
	    return projects;
	}
	
	//Get Project by Project id
	public Project getProjectById(Long pid) {
		Optional<Project> proj=repo.findById(pid);
		if(proj.isPresent()) {
			return proj.get();
		}
		return null;
	}
	
	//Delete by Project id
	public void deleteProject(Long pid) {
		repo.deleteById(pid);
	}
	
	//Mail Sender
	public void sendMail(Email email,MultipartFile attachFile){
		try {
			MimeMessage message=mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message,true);
			
			helper.setFrom("taraprasadjena40@gmail.com");
			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getMessage());
			
			if (attachFile != null && !attachFile.isEmpty()) {
				String fileName = "attachment." + getFileExtension(attachFile.getOriginalFilename());
				if("pdf".equalsIgnoreCase(getFileExtension(attachFile.getOriginalFilename()))) {
					helper.addAttachment(fileName, new ByteArrayResource(attachFile.getBytes()),"application/pdf");
				}
				else if("xml".equalsIgnoreCase(getFileExtension(attachFile.getOriginalFilename()))) {
					helper.addAttachment(fileName, new ByteArrayResource(attachFile.getBytes()),"application/xml");
				}
				else if("csv".equalsIgnoreCase(getFileExtension(attachFile.getOriginalFilename()))) {
					helper.addAttachment(fileName, new ByteArrayResource(attachFile.getBytes()),"application/csv");
				}
			}
			
			mailSender.send(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getFileExtension(String filename) {
	    int dotIndex = filename.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
	}

	//Excel converter
	public byte[] generateExcel() throws IOException{
		
		List<Project> projectList = repo.findAll();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Project info");
		HSSFRow row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("Pid");
		row.createCell(1).setCellValue("ProjName");
		row.createCell(2).setCellValue("EmpName");
		row.createCell(3).setCellValue("Role");
		row.createCell(4).setCellValue("StartDate");
		row.createCell(5).setCellValue("EndDate");
		row.createCell(6).setCellValue("Status");
		row.createCell(7).setCellValue("Email");
		
		int dataRowIndex = 1;
		
		for(Project projects: projectList) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(projects.getPid());
			dataRow.createCell(1).setCellValue(projects.getProjName());
			dataRow.createCell(2).setCellValue(projects.getEmpName());
			dataRow.createCell(3).setCellValue(projects.getRole());
			dataRow.createCell(4).setCellValue(projects.getStartDate());
			dataRow.createCell(5).setCellValue(projects.getEndDate());
			dataRow.createCell(6).setCellValue(projects.getStatus());
			dataRow.createCell(7).setCellValue(projects.getEmail());
			dataRowIndex ++;
		}
		
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream()){
			workbook.write(baos);
			return baos.toByteArray();
		}
	}
	
	//CSV file
	public void generateCsv(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=projects.csv");
		
		List<Project> projectList = repo.findAll();
		
		try(PrintWriter writer = response.getWriter()){
			writer.write("Pid,ProjName,EmpName,Role,StartDate,EndDate,Status,Email\n");
			for(Project projects: projectList) {
				writer.write(projects.getPid()+","+projects.getProjName()+","+projects.getEmpName()+","+projects.getRole()
				+","+projects.getStartDate()+","+projects.getEndDate()+","+projects.getStatus()+","+projects.getEmail()+"\n");
			}
		}
		
	}
	
	//PDF file
	public byte[] generatePdf(List<Project> projectList, HttpServletResponse response) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.setContentType("application/pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase title = new Phrase("List of Projects", fontTitle);
        PdfPTable table = createPdfTable(projectList);

        document.add(title);
        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    private PdfPTable createPdfTable(List<Project> projectList) {
        int numberOfColumns = 8;
        PdfPTable table = new PdfPTable(numberOfColumns);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
        addTableHeader(table, "PID", headerFont);
        addTableHeader(table, "ProjName", headerFont);
        addTableHeader(table, "EmpName", headerFont);
        addTableHeader(table, "Role", headerFont);
        addTableHeader(table, "StartDate", headerFont);
        addTableHeader(table, "EndDate", headerFont);
        addTableHeader(table, "Status", headerFont);
        addTableHeader(table, "Email", headerFont);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Project project : projectList) {
            addTableCell(table, String.valueOf(project.getPid()));
            addTableCell(table, project.getProjName());
            addTableCell(table, project.getEmpName());
            addTableCell(table, project.getRole());
            addTableCell(table, formatDate(project.getStartDate(), dateFormat));
            addTableCell(table, formatDate(project.getEndDate(), dateFormat));
            addTableCell(table, project.getStatus());
            addTableCell(table, project.getEmail());
        }

        return table;
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private String formatDate(LocalDate date, DateTimeFormatter formatter) {
        return date != null ? date.format(formatter) : "";
    }
}

