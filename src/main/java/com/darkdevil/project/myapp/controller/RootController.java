package com.darkdevil.project.myapp.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.darkdevil.project.myapp.model.Customer;
import com.darkdevil.project.myapp.model.Email;
import com.darkdevil.project.myapp.model.MovieSummary;
import com.darkdevil.project.myapp.service.SendEmailService;

@RestController
@RequestMapping("root")
public class RootController {

	@Autowired
	private SendEmailService sendEmailService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private RestTemplate restTemplate;
	private ArrayList<Customer> customerList = new ArrayList<Customer>();
	 //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "G://Hariharan//practice//Springboot//myapp//uploads//";
    @Value("${api.key}")
    private String API_KEY;
	
	@PostMapping(value="customers", consumes="application/json", produces="application/json")
	public Customer saveCustomer(@RequestBody Customer customer) {
		UUID id = UUID.randomUUID();
		
		customer.setId(id);
		customerList.add(customer);
		
		return customer;
	}
	
	@GetMapping(value="customers", produces="application/json")
	public List<Customer> getCustomerAllCustomers() {
		return customerList;
	}
	
	@GetMapping(value="customers/{id}", produces="application/json")
	public Customer getCustomerById1(@PathVariable String id) {
		Customer customer = new Customer();
		
		for (Customer obj : customerList) {
			String customerId = obj.getId().toString();
			if(customerId.equals(id)) {
				return obj;
			}
		}
		
		return customer;
	}
	
	@PutMapping(value="customers", consumes="application/json", produces="application/json")
	public Customer updateCustomer(@RequestParam("id") String id, @RequestBody Customer customer) {
		boolean update = false;

		for (Customer obj : customerList){
			String customerId = obj.getId().toString();
			
			if(customerId.equals(id)) {
				obj.setName(customer.getName());
				obj.setEmail(customer.getEmail());
				obj.setPhone(customer.getPhone());
				customer.setId(obj.getId());
				update = true;
			}
		}
		
		if(update == false) {
			return new Customer();
		}
		else {
			return customer;
		}
	}
	
	@PostMapping(value="file")
	public String fileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
	            System.out.println("message: Please select a file to upload");
	            return "message: Please select a file to upload!!!";
	        }
			
			int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 6;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int) 
                  (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String file_name = generatedString + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER + file_name);
            Files.write(path, bytes);
			
            return "message: File uploaded && filename: " + file_name;
		}
		catch(Exception e) {
			e.printStackTrace();
			return "message: File not uploaded!!!";
		}
	}
	
	@PostMapping("send_email")
	public String sendEmail(@RequestBody Email email) {
		sendEmailService.sendEmail(email.getTo(), email.getBody(), email.getTopic());
		return "message: Email sent...";
	}
	
	@PostMapping("send_email1")
	public String sendEmail1(@RequestBody Email email) {
		MimeMessage message = mailSender.createMimeMessage();
	    try {
	    	File file = new File("G:\\Hanarshanya\\it17108782_Assignment2.pdf");
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom("hariharansliit@gmail.com");
	        helper.setTo(email.getTo());
	        helper.setSubject(email.getTopic());
	        helper.setText(email.getBody());
	        helper.addAttachment(file.getName(), file);
//	        helper.addAttachment("MyTestFile.txt", new ByteArrayResource(content));
	        mailSender.send(message);
	    } catch (MessagingException e) {

	        e.printStackTrace();
	    }
		return "message: Email sent...";
	}
	
	@GetMapping("movies/{id}")
	public @ResponseBody MovieSummary movies(@PathVariable String id) {
		String url = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+API_KEY;
		MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);
		return movieSummary;
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
