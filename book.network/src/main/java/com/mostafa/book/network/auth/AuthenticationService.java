package com.mostafa.book.network.auth;

import com.mostafa.book.network.email.EmailService;
import com.mostafa.book.network.email.EmailTemplateName;
import com.mostafa.book.network.role.RoleRepository;
import com.mostafa.book.network.security.JWTUtils;
import com.mostafa.book.network.user.Token;
import com.mostafa.book.network.user.TokenRepository;
import com.mostafa.book.network.user.User;
import com.mostafa.book.network.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
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

    public void activateUser(String token) {
        var savedToken = tokenRepository.findByToken(token);
        if (savedToken.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        if(LocalDateTime.now().isAfter(savedToken.get().getExpiredAt())){
            throw new RuntimeException("Token expired");
        }
        var user = savedToken.get().getUser();
        user.setEnabled(true);
        user.setAccountLocked(false);
        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(@Valid LoginRequest request) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = (User) auth.getPrincipal();
            var claims = new HashMap<String, Object>();
            claims.put("FullName", user.getFullName());
            var token = jwtUtils.generateJwtToken(user, claims);

            return AuthenticationResponse.builder().token(token).build();
        } catch (Exception ex) {
            ex.printStackTrace(); // Log the error
            throw new RuntimeException("Authentication failed: " + ex.getMessage(), ex);
        }
    }
}
