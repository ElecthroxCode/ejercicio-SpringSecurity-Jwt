package com.school.school_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.school.school_app.domain.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{

	UserDetails findByUsername(String username);

}
