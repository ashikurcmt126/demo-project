package com.example.demoproject.security;

import com.example.demoproject.security.entity.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import static com.example.demoproject.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.demoproject.security.SecurityConstants.SECRET;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(applicationUser.getUsername(),
                    applicationUser.getPassword()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        ZonedDateTime expirationTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
        final String authorities = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder().setSubject(((User) authResult.getPrincipal()).getUsername())
                .setExpiration(Date.from(expirationTimeUTC.toInstant()))
                .claim("roles", authorities)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        String username = ((User) authResult.getPrincipal()).getUsername();
        //List list=UserListRepositoryImpl.getId(username);
        //com.example.security.entity.User user = userService.getUserByUsername(username);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        System.out.println(authorities);
        map.put("role", authorities.toString().replace("ROLE_", ""));
        response.getWriter().write(new JSONObject(map).toString());
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }
}