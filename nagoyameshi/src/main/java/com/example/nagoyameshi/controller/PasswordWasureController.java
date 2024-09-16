package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PasswordWasureController {
	@GetMapping("/passwordwasure")
    public String index(Model model) {
        return "auth/passwordwasure"; 
    }
}
