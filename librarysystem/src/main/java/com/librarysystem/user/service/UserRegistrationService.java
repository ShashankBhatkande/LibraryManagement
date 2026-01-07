package com.librarysystem.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarysystem.user.model.AccountStatus;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email Already Exists");
        }

        if ("User".equals(user.getRole())) {
            user.setStatus(AccountStatus.APPROVED);
        } else {
            user.setStatus(AccountStatus.PENDING);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
