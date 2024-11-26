package com.springboot.manage_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.manage_users.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String>{
	@Query("SELECT userid FROM Users ORDER BY SUBSTRING(userid, 4, 5) DESC LIMIT 1")
	String findLastUserId();
	
	@Query("SELECT u FROM Users u WHERE u.userid = :userId")
    Optional<Users> findByUserId(String userId);
}
