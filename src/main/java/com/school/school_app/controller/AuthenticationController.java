package com.school.school_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.school_app.dto.AuthLoginDTO;
import com.school.school_app.dto.AuthResponseDTO;
import com.school.school_app.service.serviceImpl.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	private UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	public AuthenticationController(UserDetailsServiceImpl userDetailsServiceImpl) {
		this.userDetailsServiceImpl = userDetailsServiceImpl;
	}
	
	@PostMapping("/log-in")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthLoginDTO authLoginDTO){
		
		return ResponseEntity.ok(userDetailsServiceImpl.loginUser(authLoginDTO));
	}

}
