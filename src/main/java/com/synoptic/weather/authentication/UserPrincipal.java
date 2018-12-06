package com.synoptic.weather.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synoptic.weather.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Customised spring security user details
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    /**
     * user authorities list
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Creates new principal user
     *
     * @param user current use
     * @return new created principal user
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new UserPrincipal(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    /**
     * Gets user authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Gets true that user account non expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Gets true that user account is non locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Gets true that user credentials is non expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Gets true that user account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Customised equals method
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Customised hashCode method
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
