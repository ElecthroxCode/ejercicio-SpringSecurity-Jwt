package com.school.school_app;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.school.school_app.domain.PermissionUser;
import com.school.school_app.domain.RoleEnum;
import com.school.school_app.domain.RolesUser;
import com.school.school_app.domain.UserEntity;
import com.school.school_app.repository.UserEntityRepository;

@SpringBootApplication
public class SchoolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolAppApplication.class, args);
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	CommandLineRunner commandLineRuner(UserEntityRepository userEntityRepository) {
		
		
		return args -> {
			//CREATE PERMISSIONS
			PermissionUser createPermission = PermissionUser.builder().name("CREATE").build();
			PermissionUser readPermission = PermissionUser.builder().name("READ").build();
			PermissionUser deletePermission = PermissionUser.builder().name("DELETE").build();
			//CREATE ROLES
			RolesUser adminRole = RolesUser.builder().roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, deletePermission)).build();
			
			RolesUser userRole = RolesUser.builder().roleEnum(RoleEnum.USER)
					.permissionList(Set.of(readPermission, createPermission)) .build();
			
			RolesUser invitedRole = RolesUser.builder()
					.roleEnum(RoleEnum.INVITED).permissionList(Set.of(readPermission)).build();
			
			//CREATE USERS
			UserEntity userManuel = UserEntity.builder()
					.name("Emmanuel")
					.username("manu2000")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(adminRole))
					.build();
			
			UserEntity userVeronica = UserEntity.builder()
					.name("Veronica")
					.username("vero000")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(userRole))
					.build();
			
			UserEntity userGustavo = UserEntity.builder()
					.name("Gustavo")
					.username("gusta0101")
					.password(passwordEncoder.encode("12345"))
					.roles(Set.of(invitedRole))
					.build();
			
			userEntityRepository.saveAll(List.of(userManuel, userGustavo, userVeronica));
			
		};
		
	}

}
