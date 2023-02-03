package com.somu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somu.entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model m)
    {
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
		new UserController().userRegister(u,m);
		return "signup";
	}
    
    @GetMapping("/login")
    public String login(Model m)
    {
    	 m.addAttribute("title", "Login Page");
    	return "login";
    }
}
