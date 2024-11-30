package com.school.school_app.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.school.school_app.domain.UserEntity;
import com.school.school_app.repository.UserEntityRepository;
import com.school.school_app.service.UserEntityService;

public class UserEntityServiceImpl implements UserEntityService{
	
	private final UserEntityRepository userEntityRepository;
	
	@Autowired
	public UserEntityServiceImpl(UserEntityRepository userEntityRepository) {
		this.userEntityRepository = userEntityRepository;
	}
	
	@Override
	public UserEntity createUser(UserEntity userEntity) {
		
		return userEntityRepository.save(userEntity);
	}

}
