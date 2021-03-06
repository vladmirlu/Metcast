package com.synoptic.weather.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * User role entity
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role name
     * */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;

    /**
     * Customised to string method
     *
     * @return current object as string
     */
    @Override
    public String toString(){
        return new StringBuilder().append(" Role: { id: ").append(id)
                .append(", name: ").append(name).toString();
    }
}
