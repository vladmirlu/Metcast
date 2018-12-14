package com.synoptic.weather.model.repository;

import com.synoptic.weather.model.entity.Role;
import com.synoptic.weather.model.entity.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/**
 * Role repository to interact with model
 * */
@Transactional
public interface RoleDao extends CrudRepository<Role, Long> {

    /**
     * Finds role by name
     *
     * @param roleName role name
     * */
    Optional<Role> findByName(RoleName roleName);
}
