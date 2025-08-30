package com.librarysystem.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApprovalApi {
    private final UserService service;

    @PatchMapping("/approve") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveUser(@RequestParam Long id) {
        return service.approveUser(id);
    }

    @DeleteMapping("/reject") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectUser(@RequestParam Long id) {
        return service.rejectUser(id);
    }
}
