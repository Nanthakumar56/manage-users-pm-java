package com.springboot.manage_users.repository;

import java.util.List;
import java.util.Map;
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
	
	@Query("SELECT u.userid, u.first_name, u.last_name, u.role, u.email, u.created_at, u.status,u.phone, u.employee_id,u.department, u.designation, u.organization_unit,u.last_login, u.username, u.password, u.profile FROM Users u WHERE u.userid = :userId")
    Optional<Users> findByUserId(@Param("userId") String userId);
	
	@Query("SELECT new map(u.userid as userid, u.first_name as first_name, u.last_name as last_name, " +
	       "u.role as role, u.email as email, u.created_at as created_at, u.status as status) " +
	       "FROM Users u")
	List<Users> findAllUserFields();

	List<Users> findByUseridIn(List<String> userIds);	

}
