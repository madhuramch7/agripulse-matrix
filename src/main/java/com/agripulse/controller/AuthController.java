package com.agripulse.controller;

import com.agripulse.entity.UserAccount;
import com.agripulse.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserAccount user) {
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered!"));
        }

        UserAccount savedUser = userRepository.save(user);
        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "userId", savedUser.getUserId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<UserAccount> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            UserAccount user = userOpt.get();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getUserId());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}