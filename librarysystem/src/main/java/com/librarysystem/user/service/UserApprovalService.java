package com.librarysystem.user.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.librarysystem.user.model.AccountStatus;
import com.librarysystem.user.model.User;
import com.librarysystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApprovalService {
    private final UserRepository userRepository;
    public void rejectUser(Long id) {
        User user = getUser(id);
        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);
    }

    public void approveUser(Long id) {
        User user = getUser(id);
        user.setStatus(AccountStatus.APPROVED);
        userRepository.save(user);
    }
    
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
