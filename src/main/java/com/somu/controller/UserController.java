package com.somu.controller;

import java.io.File;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.somu.dao.ContactRepo;
import com.somu.dao.UserRepo;
import com.somu.entity.Contact;
import com.somu.entity.User;
import com.somu.helper.Message;
import com.somu.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private ContactRepo contactRepo;

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
		m.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
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
		m.addAttribute("title", "Add Contact");
		m.addAttribute("user", u);
		m.addAttribute("contact", new Contact());

		return "normal/add_contact";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profilepic") MultipartFile img,
			HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println("DATA : " + contact);

		try {
			System.out.println(session.getAttribute("email").toString());
			User u = repo.getUserByEmail(session.getAttribute("email").toString());

			contact.setUser(u);

			if (img.isEmpty()) {
				contact.setImgUrl("default-user.jpg");
			} else {
				String imgName = u.getId() + "" + contact.getEmail() + "" + new Date().getTime();
				System.out.println(imgName);

				String originalFilename = img.getOriginalFilename();
				System.out.println("Original file name : " + originalFilename);
				int lastIndexOf = originalFilename.lastIndexOf(".");
				String substring = originalFilename.substring(lastIndexOf);

				File file = new ClassPathResource("static/img").getFile();

				String nameFIle = file.getAbsoluteFile() + File.separator + imgName + substring;
				System.out.println(imgName + substring);
				contact.setImgUrl(imgName + substring);
				Path path = Paths.get(nameFIle);

				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			u.getContacts().add(contact);

			repo.save(u);

			System.out.println(u);
			m.addAttribute("user", u);
			m.addAttribute("contact", contact);

			session.setAttribute("message", new Message("Contact added!", "success"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			session.setAttribute("message", new Message("Something went wrong!", "danger"));
			e.printStackTrace();
		}

		return "/normal/add_contact";
	}

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable Integer page, HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("title", "Show Contacts");
		m.addAttribute("user", u);
		m.addAttribute("contact", new Contact());

		PageRequest pageAble = PageRequest.of(page, 10);

		Page<Contact> contacts = contactRepo.findContactsByUser(u.getId(), pageAble);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Integer cId, HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("title", "Show Contact Details");
		m.addAttribute("user", u);
		m.addAttribute("contact", new Contact());
		System.out.println(cId);

		Contact contact = contactRepo.findById(cId).get();

		m.addAttribute("pContact", contact);

		return "normal/contact_details";
	}

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("contact", new Contact());

		Contact con = contactRepo.findById(cId).get();

		u.getContacts().remove(con);

		repo.save(u);

		session.setAttribute("message", new Message("Contact Deleted!", "success"));

		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/update/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId, HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("title", "Update Contact");

		Contact contact = contactRepo.findById(cId).get();

		m.addAttribute("contact", contact);

		return "normal/update_contact";
	}

	@PostMapping("/process-contact-update")
	public String processContactUpdate(@ModelAttribute Contact contact, @RequestParam("profilepic") MultipartFile img,
			@ModelAttribute("userid") int userid, HttpSession session, Model m) {

		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		Contact oldContact = contactRepo.findById(contact.getCId()).get();

		User u = repo.findById(userid).get();
		System.out.println(u);
		m.addAttribute("user", u);

		System.out.println("USER : " + u);

		contact.setUser(u);

		try {

			if (img.isEmpty()) {
				contact.setImgUrl(oldContact.getImgUrl());
			} else {

				File file = new ClassPathResource("static/img").getFile();

//				USING THIS DEFAULT PICTURE IS ALSO DELETED SO RESLOVE THIS LATER
				File file1 = new File(file, oldContact.getImgUrl());

				if (!oldContact.getImgUrl().equals("default-user.jpg"))
					file1.delete();

//				UPDATE PROFILE PICTURE
				String imgName = u.getId() + "" + contact.getEmail() + "" + new Date().getTime();
				System.out.println(imgName);

				String originalFilename = img.getOriginalFilename();
				System.out.println("Original file name : " + originalFilename);
				int lastIndexOf = originalFilename.lastIndexOf(".");
				String substring = originalFilename.substring(lastIndexOf);

				String nameFIle = file.getAbsoluteFile() + File.separator + imgName + substring;
				System.out.println(imgName + substring);
				contact.setImgUrl(imgName + substring);
				Path path = Paths.get(nameFIle);

				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			contactRepo.save(contact);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/profile")
	public String yourProfile(HttpSession session, Model m) {
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("title", "Profile Page");
		m.addAttribute("contact", new Contact());

		return "normal/profile";
	}

	@GetMapping("/settings")
	public String changePassword(HttpSession session, Model m) {
		System.out.println("Inside settings....");
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("title", "Profile Page");
		m.addAttribute("contact", new Contact());
		return "normal/settings";
	}

	@PostMapping("/change-password")
	public String changePasswordHandler(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session, Model m) {
		
		System.out.println("OLD PASSWORD : "+oldPassword);
		System.out.println("NEW PASSWORD : "+newPassword);
		
		if (session.getAttribute("email") == null) {
			System.out.println("Please login...");
			return "home";
		}

		System.out.println(session.getAttribute("email").toString());
		User u = repo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		
		String password = u.getPassword();
		
		if (password.equals(oldPassword)) {
			System.out.println("Password matched...");
			u.setPassword(newPassword);
			this.repo.save(u);
			session.setAttribute("messsage", new Message("Your Password Successfully Changed...", "alert-success"));
		}
		else {
			System.out.println("Password did not matched");
			session.setAttribute("messsage", new Message("Old Password Did not matched", "alert-error"));
		}
		
		return "redirect:/user/index";
	}
	
}
