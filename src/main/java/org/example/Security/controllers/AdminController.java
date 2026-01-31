package org.example.Security.controllers;

import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Qualifier("PersonApiService")
@Controller
@RequestMapping("api/admin")
public class AdminController {
    private PersonApiInterface personApiInterface;

    @Autowired
    public AdminController(PersonApiInterface personApiInterface) {
        this.personApiInterface = personApiInterface;
    }

    @GetMapping("/hiAdmin")
    public String adminPanel(Model model) {
        model.addAttribute("userName", "Admin");
        model.addAttribute("isAdmin", true);
        return "admin";
    }

    @GetMapping("")
    public ResponseEntity<String> getGreetings(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        return ResponseEntity.ok("Здравствуйте, " + principal.toString() + "!");
    }
//    @PostMapping("/create")
//    public ResponseEntity<PersonDTO> createUser(@AuthenticationPrincipal UserDetails userDetails
//    ){
//        return personApiInterface.createUser(userDetails, false);
//    }
//
//
//    @DeleteMapping("/deleteById")
//    public ResponseEntity<Void>deleteById(
//            @RequestParam(name = "id") Long id
//    ){
//        return personApiInterface.deleteById(id);
//    }
//    @DeleteMapping("/deleteAll")
//    public ResponseEntity<Void>deleteAll(){
//        return personApiInterface.deleteAll();
//    }
//
//    @GetMapping("/getTotalCount")
//    public ResponseEntity<Long>getTotalCount(){
//        return personApiInterface.getTotalCount();
//    }
//
//    @GetMapping("/existsById")
//    public ResponseEntity<Boolean>existsById(
//            @RequestParam(name="id")long id
//    ){
//        return personApiInterface.existsById(id);
//    }

}
