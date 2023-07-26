package com.example.demoproject.security;

import com.example.demoproject.security.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager))
                .addFilter(new JWTAuthorizationFilter(authenticationManager, customUserDetailService))
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/createUser").permitAll()
                .requestMatchers(HttpMethod.POST, "/send-location").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/create-user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/update-user/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/getUserData/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/getUserLocationsByDateTimeRange").hasRole("ADMIN")
                .anyRequest().permitAll();
        return http.build();
    }
}