package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.Role;
import com.synoptic.weather.database.entity.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface RoleDao extends CrudRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
