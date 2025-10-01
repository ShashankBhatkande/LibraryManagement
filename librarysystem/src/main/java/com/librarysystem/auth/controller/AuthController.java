package com.librarysystem.auth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.user.dto.LoginRequest;
import com.librarysystem.user.model.AccountStatus;
import com.librarysystem.user.model.User;
import com.librarysystem.user.service.JwtService;
import com.librarysystem.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) throws Exception {
        try {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        Optional<User> optionalUser = userService.getUser(loginRequest.email());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getStatus() != AccountStatus.APPROVED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Account not approved."));
            } else {
                if (authentication.isAuthenticated()) {
                    String token = jwtService.generateToken(loginRequest.email());
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    response.put("username", user.getFirstname() + " " + user.getLastname());
                    return ResponseEntity.ok(response);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));

    }
}
