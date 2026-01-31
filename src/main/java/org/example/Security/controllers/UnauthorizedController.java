package org.example.Security.controllers;

import org.example.Security.GlobalExceptionHandler;
import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Qualifier("PersonApiService")
@Controller
@RequestMapping("api/")
public class UnauthorizedController {
    private PersonApiInterface personApiInterface;
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public UnauthorizedController(PersonApiInterface personApiInterface, GlobalExceptionHandler globalExceptionHandler) {
        this.personApiInterface = personApiInterface;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping("/loginNew")
    public String login(Model model) {
        model.addAttribute("user", "Vasya");
        return "unauthorized";
    }
    @GetMapping("/")
    public String indexPage() {
        return "redirect:/api/loginNew"; // Перенаправление на страницу логина
    }

}
