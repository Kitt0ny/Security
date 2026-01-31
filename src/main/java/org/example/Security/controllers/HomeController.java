package org.example.Security.controllers;

import org.example.Security.models.PersonDTO;
import org.example.Security.models.authDTO.RegisterRequest;
import org.example.Security.service.PersonApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

@Qualifier("PersonApiService")//т.к. мы сделали систему гибче указав в качестве сериса-интерфейс, нужно указать его реализацию  виде класса
//таким образом  другом контроллере с тем же функционалом, но для другой платформы нужно лишь изменить qualifаir и написать новую реализацию
@Controller
@RequestMapping("api/home")
public class HomeController {
    private PersonApiInterface personApiInterface;

    @Autowired
    public HomeController(PersonApiInterface personApiInterface) {
        this.personApiInterface = personApiInterface;
    }

    @GetMapping("")
    public String homePage(Model model) {
        // Этот метод просто возвращает HTML страницу
        // JWT проверяется через JavaScript на клиенте
        return "home";
    }

    @GetMapping("/greetings")
    public ResponseEntity<String> getGreetings(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        return ResponseEntity.ok("Здравствуйте, " + principal.getName() + "!");
    }


    @GetMapping("/loginNew")
    public String login(Model model) {
        model.addAttribute("user", "Vasya");
        return "unauthorized";
    }

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
