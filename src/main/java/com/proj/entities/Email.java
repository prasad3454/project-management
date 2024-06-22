package com.proj.entities;

import java.io.Serializable;

public class Email implements Serializable{
	
	private String to;
	private String subject;
	private String message;
	//private MultipartFile attachFile;
	
//	public MultipartFile getAttachFile() {
//		return attachFile;
//	}
//	public void setAttachFile(MultipartFile attachFile) {
//		this.attachFile = attachFile;
//	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
