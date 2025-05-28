package com.festivra.ticketing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.festivra.ticketing.entity.Role;
import com.festivra.ticketing.entity.User;
import com.festivra.ticketing.repository.RoleRepository;
import com.festivra.ticketing.repository.UserRepository;

@SpringBootApplication
public class TicketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingApplication.class, args);
	}




  
  @Bean
  CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
	  return args -> {
		  if (roleRepo.findByName("ROLE_USER").isEmpty()) {
			  roleRepo.save(new Role("ROLE_USER"));
		  }
  
		  if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
			  roleRepo.save(new Role("ROLE_ADMIN"));
		  }
  
		  if (roleRepo.findByName("ROLE_AGENT").isEmpty()) {
			  roleRepo.save(new Role("ROLE_AGENT"));
		  }
  
		  if (userRepo.findByEmail("admin@festivra.com").isEmpty()) {
			  User admin = new User();
			  admin.setEmail("admin@festivra.com");
			  admin.setPassword(encoder.encode("admin123"));
			  admin.setFullName("Festivra Admin");
			  admin.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
			  userRepo.save(admin);
		  }
	  };
  }

  
  @Bean
  public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
  }
}