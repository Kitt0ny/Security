package org.example.Security.service;

import org.example.Security.models.PersonDTO;
import org.example.Security.models.authDTO.AuthRequest;
import org.example.Security.models.authDTO.AuthResponse;
import org.example.Security.models.authDTO.RefreshRequest;
import org.example.Security.models.authDTO.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

public interface PersonApiInterface {//интерфейс укажет если в какойто из реализациии мы забыли переопределить метод
//    ResponseEntity<PersonDTO> createUser(UserDetails userDetails, boolean isAdmin);
    AuthResponse refresh(RefreshRequest request);
    AuthResponse login(AuthRequest request);
    PersonDTO createUser(RegisterRequest request);
   }
