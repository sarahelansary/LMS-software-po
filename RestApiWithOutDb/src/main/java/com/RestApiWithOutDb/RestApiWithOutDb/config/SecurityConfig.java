package com.RestApiWithOutDb.RestApiWithOutDb.config;
import com.RestApiWithOutDb.RestApiWithOutDb.CustomUserDetailedService.CustomUserDetailedService;
import com.RestApiWithOutDb.RestApiWithOutDb.service.Services;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final Services services;

    public SecurityConfig(Services services) {
        this.services = services;
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailedService(services);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder disables password encoding (plain text password)
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService());
        provider.setPasswordEncoder(passwordEncoder());  // Use NoOpPasswordEncoder
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Use lambda to disable CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(new AntPathRequestMatcher("/api/register")).permitAll()  // Allow public access to /register
                        .anyRequest().authenticated()  // Require authentication for other endpoints
                )
                .httpBasic(withDefaults());  // Use HTTP Basic authentication

        return http.build();
    }
}
