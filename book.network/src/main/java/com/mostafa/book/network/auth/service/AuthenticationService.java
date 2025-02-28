package com.mostafa.book.network.auth.service;

import com.mostafa.book.network.auth.model.RegisterationRequest;
import com.mostafa.book.network.email.EmailService;
import com.mostafa.book.network.email.EmailTemplateName;
import com.mostafa.book.network.role.RoleRepository;
import com.mostafa.book.network.user.Token;
import com.mostafa.book.network.user.TokenRepository;
import com.mostafa.book.network.user.User;
import com.mostafa.book.network.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    public void register(RegisterationRequest request) throws MessagingException {
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

        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                "Acrivate your account",
                user.getFullName(),
                "http://localhost:4200/activate?token=" + token,
                token,
                EmailTemplateName.ACTIVATE_ACCOUNT

        );
    }

    private String generateAndSaveActivationToken(User user) {
        String activationToken = generateEmailActivationToken(6);
        Token token = Token.builder()
                .token(activationToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);
        return activationToken;
    }

    private String generateEmailActivationToken(int length) {
        String Characters = "012346789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(Characters.length());
            token.append(Characters.charAt(randomIndex));
        }
        return token.toString();

    }
}
