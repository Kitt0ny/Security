package org.example.Security.service;

import org.example.Security.models.Person;
import org.example.Security.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public boolean userExists(String username) {
        return personRepository.existsByLogin(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user: " + username); // ← Отладка

        Person person = personRepository.findPersonByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        System.out.println("User found: " + person.getLogin() + ", role: " + person.getRole()); // ← Отладка

        return org.springframework.security.core.userdetails.User.builder()
                .username(person.getLogin())
                .password(person.getPassword())
                .authorities(person.getRole()) // Должно быть "ROLE_USER" или "ROLE_ADMIN"
                .build();
    }
}