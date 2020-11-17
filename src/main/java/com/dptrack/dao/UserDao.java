package com.dptrack.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dptrack.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{

	User findByEmail(String email);

	User findByUserId(String id);

}
