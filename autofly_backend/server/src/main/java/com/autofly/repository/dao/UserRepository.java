package com.autofly.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autofly.repository.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	User findByEmailIdAndPassword(String emailId, String password);
}
