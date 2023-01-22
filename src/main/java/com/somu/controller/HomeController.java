package com.somu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
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
}
