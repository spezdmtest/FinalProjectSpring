package com.griddynamics.finalprojectspring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.griddynamics.finalprojectspring.entities.User;
import com.griddynamics.finalprojectspring.repositories.UserRepository;


public class UserServiceImplTest {
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void SetUpBeforeEach() {
        System.out.println("Before each test");
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        repository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(repository, passwordEncoder);
    }

    @Test
    void checkFindByName() {
        String name = "my@email.com";
        User expectedUser = User.builder().id(1L).email(name).build();
        Mockito.when(repository.findFirstByEmail(Mockito.anyString())).thenReturn(expectedUser);
        User actualUser = userService.findByName(name);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }
}


