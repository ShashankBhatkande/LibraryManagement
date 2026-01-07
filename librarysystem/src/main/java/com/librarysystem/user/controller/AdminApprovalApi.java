package com.librarysystem.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarysystem.user.service.UserApprovalService;
import com.librarysystem.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminApprovalApi {
    private final UserService service;
    private final UserApprovalService  approvalService;
    @PatchMapping("/approve")
    public ResponseEntity<Map<String, String>> approveUser(@RequestParam Long id) {
        approvalService.approveUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User approved."));
    }

    @PatchMapping("/delete")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam Long id) {
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User approved."));
    }

    @DeleteMapping("/reject")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> rejectUser(@RequestParam Long id) {
        approvalService.rejectUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "User approved."));
    }
}
