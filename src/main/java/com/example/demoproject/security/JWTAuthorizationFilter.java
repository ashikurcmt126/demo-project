package com.example.demoproject.security;

import com.example.demoproject.security.entity.ApplicationUser;
import com.example.demoproject.security.service.impl.CustomUserDetailService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static com.example.demoproject.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final CustomUserDetailService customUserDetailService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuth = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if (token == null) return null;
        String username = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX," "))
                .getBody()
                .getSubject();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
        ApplicationUser applicationUser = customUserDetailService.loadApplicationUserByUserName(username);
        return username != null ? new UsernamePasswordAuthenticationToken(username,null, AuthorityUtils.createAuthorityList(userDetails.getAuthorities().iterator().next().toString())) : null;
    }
}
