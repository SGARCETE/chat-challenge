package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.exceptions.UserAlreadyExistsException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.impl.DefaultUsersService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultUsersServiceTest {
    private UsersService userService;
    private PasswordEncoder passwordEncoder;
    private UsersRepository userRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new DefaultUsersService(passwordEncoder, userRepository);
    }

    @Test
    public void testCreateUserThenReturnSameUser() {
        User expectedUser = buildNewUser();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        User response = userService.createUser(expectedUser);
        assertEquals(expectedUser, response);
    }

    @Test
    public void testCreateUserThenThrowsUserAlreadyExistsException() {
        User expectedUser = buildNewUser();
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(expectedUser));
        Throwable ex = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(expectedUser);});
        assertEquals(String.format("The user with name %s already exists", expectedUser.getUserName()), ex.getMessage());
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}
