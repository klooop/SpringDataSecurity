package com.example.springdatasecurity.services;

import com.example.springdatasecurity.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
public interface UserService extends UserDetailsService {
    User findByUserName(String username);
}
