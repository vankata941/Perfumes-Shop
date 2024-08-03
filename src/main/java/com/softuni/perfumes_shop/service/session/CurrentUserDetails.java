package com.softuni.perfumes_shop.service.session;

import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentUserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;


    public Optional<User> optCurrentUser() {
        return userRepository.findByUsername(getUserDetails().getUsername());
    }

    public UserDetails getUserDetails() {
        return (UserDetails) getAuthentication().getPrincipal();
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean hasRole(String role) {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX + role));
    }

    public boolean isAuthenticated() {
        return !hasRole("ANONYMOUS");
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().getAuthorities();
    }
}
