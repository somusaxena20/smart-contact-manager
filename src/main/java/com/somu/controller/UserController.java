package com.somu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somu.entity.User;
import com.somu.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping("/index")
	public String dashboard() {
		return "normal/user_dashboard";
	}

	public void userRegister(User u, Model m) {
		u.setRole("ROLE_USER");
		u.setEnabled(true);

		User result = service.save(u);

		m.addAttribute("user", result);
	}
	
	@PostMapping("/validate")
	public String validate(@ModelAttribute("email") String email, @ModelAttribute("password") String password) {
		System.out.println("Email : "+email+" password : "+password);
		
		String url = "jdbc:mysql://localhost:3306/smart_contact_manager";
		String user = "root";
		String pass = "root";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con = DriverManager.getConnection(url, user, pass);
			
			PreparedStatement pstmt = con.prepareStatement("SELECT user_email, user_password FROM user");
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(email.equals(rs.getString("user_email"))) {
					if(password.equals(rs.getString("user_password"))) {
						System.out.println("USER FOUND");
						return "normal/user_dashboard";
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
}
