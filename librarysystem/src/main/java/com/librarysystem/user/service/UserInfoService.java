package com.librarysystem.user.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.librarysystem.user.model.User;
import com.librarysystem.user.model.UserInfoDetails;
import com.librarysystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService{
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userinfo = userRepository.findByEmail(username);
        if(userinfo.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email:" + username);
        }
        
        return new UserInfoDetails(userinfo.get());
    }
}
