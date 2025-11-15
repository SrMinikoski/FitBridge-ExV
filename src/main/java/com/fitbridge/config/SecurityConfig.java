package com.fitbridge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Libera TODOS os endpoints
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // Remove login Basic
                .formLogin(formLogin -> formLogin.disable()); // Remove tela de login

        return http.build();
    }
}
