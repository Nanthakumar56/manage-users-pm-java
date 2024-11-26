package com.springboot.manage_users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.manage_users.entity.Documents;

public interface DocumentsRepository extends JpaRepository<Documents, String> {

	@Query("SELECT docs FROM Documents docs WHERE docs.userid = :userid")
	List<Documents> findByUserId(String userid);

}
