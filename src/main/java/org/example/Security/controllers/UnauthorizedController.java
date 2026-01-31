package org.example.Security.controllers;

import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Qualifier("PersonApiService")
@Controller
@RequestMapping("/api")
public class UnauthorizedController {

    private final PersonApiInterface personApiInterface;

    @Autowired
    public UnauthorizedController( PersonApiInterface personApiInterface) {
        this.personApiInterface = personApiInterface;
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model) {
        return "unauthorized"; // Возвращает unauthorized.html
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/api/loginPage";
    }

    @GetMapping("/registrationPage")
    public String registrationPage(Model model) {
        return "registration";
    }
}
