package com.mostafa.book.network.auth.controller;

import com.mostafa.book.network.auth.model.RegisterationRequest;
import com.mostafa.book.network.auth.service.AuthenticationService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register (
            @RequestBody @Valid RegisterationRequest request
    ) throws MessagingException {
         service.register(request);
         return ResponseEntity.accepted().build();
    }



}
