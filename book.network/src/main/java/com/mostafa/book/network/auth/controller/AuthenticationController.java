package com.mostafa.book.network.auth.controller;

import com.mostafa.book.network.auth.model.AuthenticationResponse;
import com.mostafa.book.network.auth.model.LoginRequest;
import com.mostafa.book.network.auth.model.RegisterationRequest;
import com.mostafa.book.network.auth.service.AuthenticationService;
import com.mostafa.book.network.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthenticationController {


    private final AuthenticationService service;
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register (
            @RequestBody @Valid RegisterationRequest request
    ) throws MessagingException {
         service.register(request);
         return ResponseEntity.accepted().build();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid LoginRequest request) {
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> activateAccount (@RequestParam String token) {
        service.activateUser(token);
        return ResponseEntity.accepted().build();
    }



}
