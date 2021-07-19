package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.exceptions.PasswordNotValidException;
import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = buildExpectedUser();
    }

    @Test
    void testAuthUserSuccessfullyWithStatus200ReturnsUserId() throws Exception {
        doReturn(user).when(authService).authUser(anyString(), anyString());

        mockMvc.perform(get("/users/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
        ;
    }

    @Test
    void testGetUserByIdFailWithStatus404ReturnsUserNotFoundException() throws Exception {

        UserNotFoundException expectedException = new UserNotFoundException(String.format("The user with name %s does not exists",
                user.getUserName()));

        doThrow(expectedException).when(authService).authUser(user.getUserName(), user.getPassword());

        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User not found Exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("404"))
        ;
    }

    @Test
    void testAuthUserPasswordFailWithStatus400ReturnsPasswordNotValidException() throws Exception {

        PasswordNotValidException expectedException = new PasswordNotValidException(String.format("Password not valid for user %s",
                user.getUserName()));

        doThrow(expectedException).when(authService).authUser(user.getUserName(), user.getPassword());

        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Password not valid exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("400"))
        ;
    }

    private User buildExpectedUser() {
        return new User()
                .setId(1L)
                .setUserName("Santiago")
                .setPassword("Garcete");
    };

}
