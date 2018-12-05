package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.Role;
import com.synoptic.weather.database.entity.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/**
 * Role repository to interact with database
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
