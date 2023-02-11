package com.somu.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.somu.dao.UserRepo;
import com.somu.entity.Contact;
import com.somu.entity.User;
import com.somu.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private UserRepo repo;

	@GetMapping("/index")
	public String dashboard(HttpSession session, Model m) {

		System.out.println(session.isNew());

		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		return "normal/user_dashboard";
	}

	public void userRegister(User u, Model m) {
		u.setRole("ROLE_USER");
		u.setEnabled(true);

		User result = service.save(u);

		m.addAttribute("user", result);
	}

	@PostMapping("/validate")
	public String validate(@ModelAttribute("email") String email, @ModelAttribute("password") String password,
			HttpSession session) {

		System.out.println("Email : " + email + " password : " + password);

		String url = "jdbc:mysql://localhost:3306/smart_contact_manager";
		String user = "root";
		String pass = "root";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection(url, user, pass);

			PreparedStatement pstmt = con.prepareStatement("SELECT user_email, user_password FROM user");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				if (email.equals(rs.getString("user_email"))) {
					if (password.equals(rs.getString("user_password"))) {
						System.out.println("USER FOUND");

						session.setAttribute("email", email);
						return "redirect:/user/index";
					}
				}
			}
			System.out.println("USER NOT FOUND");
			return "home";

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "home";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if (session.getAttribute("email") == null) {
			return "login";
		}

		session.removeAttribute("email");

		return "login";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(HttpSession session, Model m) {
		System.out.println(session.isNew());

		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("contact", new Contact());

		return "normal/add_contact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact , @RequestParam("profilepic") MultipartFile img , HttpSession session, Model m)
	{
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}
		
		System.out.println("DATA : " +contact);
		
		
		try {
			System.out.println(session.getAttribute("email").toString());
			User u = repo.getUserByEmail(session.getAttribute("email").toString());
			
			contact.setUser(u);
			
			if(img.isEmpty())
			{
				contact.setImgUrl("default-user.jpg");
			}
			else {
				String imgName = u.getId()+""+ contact.getEmail()+""+new Date().getTime();
				System.out.println(imgName);
				
				contact.setImgUrl(imgName);
				
				String originalFilename = img.getOriginalFilename();
				System.out.println("Original file name : "+originalFilename);
				int lastIndexOf = originalFilename.lastIndexOf(".");
				String substring = originalFilename.substring(lastIndexOf);
				
				File file = new ClassPathResource("static/img").getFile();
				
				String nameFIle = file.getAbsoluteFile()+File.separator+imgName+substring;
				System.out.println(nameFIle);
				Path path = Paths.get(nameFIle);
				
				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			u.getContacts().add(contact);
			
			repo.save(u);

			System.out.println(u);
			m.addAttribute("user", u);
			m.addAttribute("contact", contact);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/normal/user_dashboard";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
