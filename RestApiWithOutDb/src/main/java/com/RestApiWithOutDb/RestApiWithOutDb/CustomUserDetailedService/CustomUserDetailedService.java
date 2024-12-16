package com.RestApiWithOutDb.RestApiWithOutDb.CustomUserDetailedService;

import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.RestApiWithOutDb.RestApiWithOutDb.model.Users;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CustomUserDetailedService implements UserDetailsService {

    private final Services services;

    public CustomUserDetailedService(Services services) {
        this.services = services;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ensure you are fetching users correctly
        Users user = services.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username)) // Use the username as per your login process
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Verify what data is being returned
        System.out.println("Loaded User: " + user.getUsername() + " | Password: " + user.getPassword());
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        // Build UserDetails
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())  // Ensure this matches the format in the login request
                .authorities(authorities)     // Ensure roles are properly set
                .build();
    }
}

