package com.ra.session05;

import com.ra.session05.model.entity.Role;
import com.ra.session05.model.entity.RoleName;
import com.ra.session05.model.entity.User;
import com.ra.session05.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Session05Application {

	public static void main(String[] args) {
		SpringApplication.run(Session05Application.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder, IUserRepository userRepository) {
//		return args -> {
//			Role adminRole = new Role(null, RoleName.ROLE_ADMIN);
//			Role user = new Role(null, RoleName.ROLE_USER);
//			Role manager = new Role(null, RoleName.ROLE_MANAGER);
//			Set<Role> roles = new HashSet<>();
//			roles.add(adminRole);
//			roles.add(user);
//			roles.add(manager);
//
//			User roleAdmin = new User(null,"admin123","admin",null,passwordEncoder.encode("admin123"),true,roles);
//			userRepository.save(roleAdmin);
//		};
//	}
}
