package net.bst.springboot.springsecurity.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import net.bst.springboot.springsecurity.model.User;
import net.bst.springboot.springsecurity.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
