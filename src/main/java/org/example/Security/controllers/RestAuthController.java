package org.example.Security.controllers;

import org.example.Security.models.PersonDTO;
import org.example.Security.models.authDTO.*;
import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Qualifier("PersonApiService")
@RestController
@RequestMapping("api/auth")
public class RestAuthController {


    private final PersonApiInterface personService;

    public RestAuthController(PersonApiInterface personService) {
        this.personService = personService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("Login attempt: " + request.login()); // ← Для отладки
            AuthResponse response = personService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // ← Выведет стек ошибки в консоль
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
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
