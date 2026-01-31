package org.example.Security.service;

import com.github.javafaker.Faker;
import org.example.Security.models.Person;
import org.example.Security.PersonRepository;
import org.example.Security.models.PersonDTO;
import org.example.Security.models.authDTO.AuthRequest;
import org.example.Security.models.authDTO.AuthResponse;
import org.example.Security.models.authDTO.RefreshRequest;
import org.example.Security.models.authDTO.RegisterRequest;
import org.example.Security.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class PersonApiService implements PersonApiInterface {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public PersonApiService(PersonRepository personRepository, PasswordEncoder passwordEncoder, Faker faker, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.faker = faker;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        generationData();
        System.out.println(personRepository.count()>0?"пользователи в бд появились или были":"БД пуста");
    }

    public PersonDTO createUser(RegisterRequest registerRequest) {
        if (personRepository.existsByLogin(registerRequest.login())) {
            throw new IllegalArgumentException("User already exists");
        }

        Person person = new Person(
                registerRequest.login(),
                passwordEncoder.encode(registerRequest.password()),
                registerRequest.email(),
                "ROLE_USER",
                LocalDateTime.now()
        );

        personRepository.save(person);
        return person.toDto();
    }
    public AuthResponse login(AuthRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password()
                )
        );

        String role = auth.getAuthorities().iterator().next().getAuthority();

        return new AuthResponse(
                jwtService.generateAccessToken(auth.getName(), role),
                jwtService.generateRefreshToken(auth.getName())
        );
    }
    public AuthResponse refresh(RefreshRequest request) {

        var claims = jwtService.parseToken(request.refreshToken());
        String username = claims.getSubject();

        String role = "ROLE_USER";

        return new AuthResponse(
                jwtService.generateAccessToken(username, role),
                jwtService.generateRefreshToken(username)
        );
    }

    public void generationData(){
        Random random = new Random();
        if(personRepository.count()==0){
            if (!personRepository.existsByLogin("daniil")) {
                personRepository.save(new Person("daniil",passwordEncoder.encode("daniil123"),"qvaqva@gmail.com","USER",LocalDateTime.now()));
            }
            if (!personRepository.existsByLogin("elena")) {
                personRepository.save(new Person("elena",passwordEncoder.encode("elena123"),"qvaqva1@gmail.com","ADMIN",LocalDateTime.now()));
            }
            if (!personRepository.existsByLogin("mikhail")) {
                personRepository.save(new Person("mikhail",passwordEncoder.encode("mikhail123"),"qvaqva2@gmail.com","USER",LocalDateTime.now()));
            }
            if (!personRepository.existsByLogin("kittony")) {
                personRepository.save(new Person("kittony",passwordEncoder.encode("kittony123"),"qvaqva3@gmail.com","ADMIN",LocalDateTime.now()));
            }
            for (int i = 0; i < 10; i++) {
                String login = faker.name().username();
                String password=passwordEncoder.encode(faker.pokemon().name());
                String domain=faker.internet().safeEmailAddress();
                String role=random.nextBoolean()?"USER":"ADMIN";
                LocalDateTime registrationDate=LocalDateTime.now();
                Person person=new Person(login,password,domain,role,registrationDate);
                personRepository.save(person);
            }
        }
    }
//    @Override//
//    public ResponseEntity<PersonDTO> createUser(UserDetails userDetails, boolean isAdmin) {
//        Person person=new Person(userDetails.getUsername(), userDetails.getPassword(), "abracadabra@gmail.com",isAdmin?"ADMIN":"USER",LocalDateTime.now());
//        if (personRepository.existsByLogin(person.getLogin())) return ResponseEntity.badRequest().build();
//        try {
//            person = personRepository.save(person);
//            URI location = ServletUriComponentsBuilder
//                    .fromCurrentContextPath()// Берет базовый URL: http://localhost
//                    .path("/api/home/getById")// Добавляет путь → /api/home/getById
//                    .queryParam("id", person.getId())
//                    .build()
//                    .toUri();
//            return ResponseEntity.created(location).body(person.toDto());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(null);
//        }
//    }
}