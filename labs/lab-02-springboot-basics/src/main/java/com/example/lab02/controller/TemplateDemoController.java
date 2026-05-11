package com.example.lab02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view")
public class TemplateDemoController {

    @GetMapping("/hello")
    public String hello(
            @RequestParam(name = "name", required = false, defaultValue = "guest") String name,
            Model model) {
        model.addAttribute("title", "thymeleaf demo");
        model.addAttribute("name", name);
        model.addAttribute("note", "this page is rendered by a template engine");
        return "hello";
    }
}
