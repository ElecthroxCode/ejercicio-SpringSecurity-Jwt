package com.school.school_app.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.school_app.dto.AuthLoginDTO;
import com.school.school_app.dto.AuthResponseDTO;
import com.school.school_app.repository.UserEntityRepository;
import com.school.school_app.util.UtilSecurityJWT;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private UtilSecurityJWT utilSecurityJWT;
	private PasswordEncoder passwordEncoder;
	private UserEntityRepository userEntityRepository;
	@Autowired
	public UserDetailsServiceImpl(UserEntityRepository userEntityRepository, UtilSecurityJWT utilSecurityJWT, PasswordEncoder passwordEncoder) {
		this.userEntityRepository = userEntityRepository;
		this.utilSecurityJWT = utilSecurityJWT;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return this.userEntityRepository.findByUsername(username);
	}

	public AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO) {
		
		
		
		String username = authLoginDTO.username();
		String password = authLoginDTO.password();
		Authentication authenticaction = this.authenticateUser(authLoginDTO.username(), authLoginDTO.password());
		SecurityContextHolder.getContext().setAuthentication(authenticaction);
		
		String accesoToken = utilSecurityJWT.createToken(authenticaction);
		return new AuthResponseDTO(username, "User loged succesfuly", accesoToken, true);
	}

	
	public Authentication authenticateUser(String username, String password) {
		UserDetails userDetails = this.loadUserByUsername(username);
		if(userDetails == null) {
			throw  new BadCredentialsException("Invalid username o password");
		}
		
		if(!this.passwordEncoder.matches(password, userDetails.getPassword())) {
			throw  new BadCredentialsException("Invalid password");
		}
		
		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
	}
}
