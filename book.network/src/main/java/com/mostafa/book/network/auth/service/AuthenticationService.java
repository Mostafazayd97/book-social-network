package com.mostafa.book.network.auth.service;

import com.mostafa.book.network.auth.model.RegisterationRequest;
import com.mostafa.book.network.role.RoleRepository;
import com.mostafa.book.network.user.User;
import com.mostafa.book.network.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void register(RegisterationRequest request) {
        var userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("User role not found"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .accountLocked(false)
                .enabled(false)
                .build();
        userRepository.save(user);

        sendValidationEmail(user)

    }
}
