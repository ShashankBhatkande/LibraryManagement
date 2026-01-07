package com.librarysystem.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.user.model.User;
import com.librarysystem.user.service.UserRegistrationService;
import com.librarysystem.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final UserRegistrationService userRegistrationService;
    @PostMapping("/save-user")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody User user) {
        userRegistrationService.registerUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User registered successfully."));
    }

    @GetMapping("/get-users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.fetchAllUsers();
    }
}
