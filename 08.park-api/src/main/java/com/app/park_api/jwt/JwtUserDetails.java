package com.app.park_api.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

// carry around just enough information for the framework to make
// authentication and authorization decisions based on the user representation
// of the application
public class JwtUserDetails extends User {

    // using the full path here to prevent conflict with the user from spring security
    private final com.app.park_api.entity.User user;

    public JwtUserDetails(com.app.park_api.entity.User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public Long getId() {
        return this.user.getId();
    }

    public String getRole() {
        return this.user.getRole().name();
    }
}
