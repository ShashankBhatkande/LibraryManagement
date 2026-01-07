package com.librarysystem.user.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }   
        userRepository.deleteById(id);
    }

}