package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.UsersService;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.impl.DefaultUsersService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultUsersServiceTest {
    private UsersService userService;
    private UsersRepository userRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        userService = new DefaultUsersService(userRepository);
    }

    @Test
    public void testCreateUserThenReturnSameUser() {
        User expectedUser = buildNewUser();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        User response = userService.createUser(expectedUser);
        assertEquals(expectedUser, response);
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}
