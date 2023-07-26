package com.example.demoproject.security.service.impl;

import com.example.demoproject.security.Role;
import com.example.demoproject.security.entity.ApplicationUser;
import com.example.demoproject.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    private  String role ="";
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        ApplicationUser applicationUser = loadApplicationUserByUserName(userName);
        return new User(applicationUser.getUsername(),applicationUser.getPassword(), AuthorityUtils.createAuthorityList("ROLE_"+role));
    }

    public ApplicationUser loadApplicationUserByUserName(String userName)
    {
        com.example.demoproject.security.entity.User user = userService.getUserByUsername(userName);
        role = Role.values()[user.getRoles()].toString();
        return new ApplicationUser(user.getEmail(),user.getPassword());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}