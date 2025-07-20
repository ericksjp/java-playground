package com.app.park_api.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.park_api.entity.User;
import com.app.park_api.service.UserService;

import lombok.RequiredArgsConstructor;

// the only purpose of “UserDetailsService” is to provide a “UserDetails”
// object to other components of the spring security framework.
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.findByUsername(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated(String username) {
        User.Role role = service.findByUsername(username).getRole();
        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
    
}
