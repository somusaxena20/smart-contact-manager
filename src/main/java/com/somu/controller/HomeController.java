package com.somu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.somu.entity.User;
import com.somu.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserService service;

    @GetMapping("/")
    public String home(Model m, HttpSession session)
    {
    	if(session.getAttribute("email") != null) {
			return "redirect:/user/index";
		}
    	
        m.addAttribute("title", "Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m)
    {
        m.addAttribute("title", "Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signUp(Model m)
    {
        m.addAttribute("title", "Register - Smart Contact Manager");
        m.addAttribute("user", new User());
        return "signup";
    }
    
    @PostMapping("/register")
	public String register(@ModelAttribute("user") User u, Model m, HttpSession session) {
		
    	u.setRole("ROLE_USER");
		u.setEnabled(true);
		u.setImgUrl("default-user.jpg");
		User result = service.saveUser(u);

		m.addAttribute("user", result);
    	
    	
		return "signup";
	}
    
    @GetMapping("/login")
    public String login(Model m)
    {
    	 m.addAttribute("title", "Login Page");
    	return "login";
    }
}
