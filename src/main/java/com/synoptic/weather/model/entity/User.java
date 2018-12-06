package com.synoptic.weather.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

/**
 * User entity
 * */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    /**
     * user unique nick name
     * */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * user unique email
     * */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * user password
     * */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * user roles
     * */
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
