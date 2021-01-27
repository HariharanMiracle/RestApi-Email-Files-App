package com.darkdevil.project.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
	
	// Setup Gmail App password, check dependency and check application.properties
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String to, String body, String topic) {
		System.out.println("Sending Email...");
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("hariharansliit@gmail.com");
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(topic);
		simpleMailMessage.setText(body);
		javaMailSender.send(simpleMailMessage);
		System.out.println("Email Sent...");
	}
}
