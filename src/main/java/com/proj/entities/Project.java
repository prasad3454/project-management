package com.proj.entities;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_table")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pid;
//	@NotBlank(message = "Enter a valid Project Name")
//	@Size(min = 2,max = 30, message = "project name must be 2 to 30")
	private String projName;
	
//	@NotBlank(message = "Enter a valid Employee Name")
//	@Size(min = 2,max = 20,message = "Employee name must be 2 to 20")
	private String empName;
	
//	@NotBlank(message = "Enter a valid Employee role")
	private String role;
//	@NotBlank(message = "Enter a valid Project start date")
	private LocalDate startDate;
	
//	@NotBlank(message = "Enter a valid Project end date")
	private LocalDate endDate;
	
//	@NotBlank(message = "Enter a valid Employee project status")
	private String status;
	
//	@NotBlank(message = "Enter a valid Employee email")
//	@Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$")
	private String email;
	
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Project [pid=" + pid + ", projName=" + projName + ", empName=" + empName + ", role=" + role
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + ", email=" + email + "]";
	}
	
	
}
