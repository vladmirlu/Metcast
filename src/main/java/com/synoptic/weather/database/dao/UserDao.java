package com.synoptic.weather.database.dao;

import com.synoptic.weather.database.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
