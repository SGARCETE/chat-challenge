package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.exceptions.PasswordNotValidException;
import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.impl.DefaultAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultAuthServiceTest {
    private AuthService authService;
    private UsersRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private User user;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authService = new DefaultAuthService(passwordEncoder, userRepository);
        user = buildNewUser();
    }

    @Test
    public void testAuthUserThenReturnUser() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        User response = authService.authUser(this.user.getUserName(), this.user.getPassword());
        assertEquals(this.user, response);
    }

    @Test
    public void testAuthUserThenThrowsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        Throwable ex = assertThrows(UserNotFoundException.class, () -> {authService.authUser(user.getUserName(), user.getPassword());});
        assertEquals(String.format("The user with name %s does not exists", user.getUserName()), ex.getMessage());
    }

    @Test
    public void testAuthUserThenThrowsPasswordNotValidException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));

        Throwable ex = assertThrows(PasswordNotValidException.class, () -> {authService.authUser(user.getUserName(), "abc");});
        assertEquals(String.format("Password not valid for user %s", user.getUserName()), ex.getMessage());
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}
