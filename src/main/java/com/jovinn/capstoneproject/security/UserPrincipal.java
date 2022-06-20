package com.jovinn.capstoneproject.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovinn.capstoneproject.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(UUID id, String firstName, String lastName, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;

        if (authorities == null) {
            this.authorities = null;
        } else {
            this.authorities = new ArrayList<>(authorities);
        }
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getActivityType().stream()
                .map(role -> new SimpleGrantedAuthority(role.getActivityType().name())).collect(Collectors.toList());

        return new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPassword(), authorities);
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : new ArrayList<>(authorities);
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
