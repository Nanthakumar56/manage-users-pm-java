package com.springboot.manage_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.manage_users.entity.ProfileImg;

public interface ProfileImgRepository  extends JpaRepository<ProfileImg, String>  {
	@Query("SELECT pimg FROM ProfileImg pimg WHERE pimg.user_id = :userId")
	Optional<ProfileImg> findByUserId(String userId);
}
