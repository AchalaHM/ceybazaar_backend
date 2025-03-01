package com.CeyBazaar.backend.config;

import com.CeyBazaar.backend.util.JwtAuthenticationFilter;
import com.CeyBazaar.backend.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    // Constructor injection
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/User/**").permitAll()
                        .requestMatchers("/Products/ViewProductList/**").permitAll()
                        .requestMatchers("/Products/ViewProductCatList/**").permitAll()
                        .requestMatchers("/home/ec2-user/uploads/**").permitAll()
                        .requestMatchers("/Products/Product/**").permitAll()
                        .requestMatchers("/Region/ViewDeliveryRegions").permitAll()
                        .requestMatchers("/Orders/NewOrder").permitAll()
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}
