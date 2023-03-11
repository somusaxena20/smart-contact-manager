package com.somu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somu.dao.ContactRepo;
import com.somu.dao.UserRepo;
import com.somu.entity.Contact;
import com.somu.entity.User;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private ContactRepo cRepo;
	
	@GetMapping("/{query}")
	public ResponseEntity<?> searchUser(@PathVariable("query") String query, HttpSession session, Model m){
		System.out.println(session.getAttribute("email").toString());
		User u = uRepo.getUserByEmail(session.getAttribute("email").toString());
		System.out.println(u);
		m.addAttribute("user", u);
		m.addAttribute("title", "Profile Page");
		m.addAttribute("contact", new Contact());
		
		List<Contact> contacts = this.cRepo.findByNameContainingAndUser(query, u);
		
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}
	
}
