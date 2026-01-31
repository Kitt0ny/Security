package org.example.Security.controllers;

import org.example.Security.models.PersonDTO;
import org.example.Security.models.authDTO.*;
import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
@Qualifier("PersonApiService")
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final PersonApiInterface personService;

    public AuthController(PersonApiInterface personService) {
        this.personService = personService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return personService.login(request);
    }

    @PostMapping("/register")
    public PersonDTO register(@RequestBody RegisterRequest request) {
        return personService.createUser(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
       return personService.refresh(request);
    }
}
