package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.MessageContent;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.dtos.MessageDto;
import com.asapp.backend.challenge.enums.MessageContentType;
import com.asapp.backend.challenge.services.MessagesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessagesController.class)
@ActiveProfiles("test")
public class MessagesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessagesService messagesService;

    private User user;
    private MessageDto messageDto;
    private Message message;

    @BeforeEach
    void setUp() {
        user = buildNewUser();
        message = buildNewMessage();
        messageDto = buildNewMessageDto();
    }

    @Test
    void testSendMessageSuccessfullyWithStatus200ReturnsMessageId() throws Exception {
        doReturn(message).when(messagesService).sendMessage(any(MessageContent.class), anyLong(), anyLong());

        mockMvc.perform(post("/messages")
                .content(objectMapper.writeValueAsString(messageDto))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
        ;
    }

    @Test
    void testSendMessageFailWithStatus404ReturnsUserNotFoundException() throws Exception {

        UserNotFoundException expectedException = new UserNotFoundException(String.format("The user with name %s does not exists",
                user.getUserName()));

        doThrow(expectedException).when(messagesService).sendMessage(any(MessageContent.class), anyLong(), anyLong());

        mockMvc.perform(post("/messages")
                .content(objectMapper.writeValueAsString(messageDto))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User not found Exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("404"))
        ;
    }

    @Test
    void testSearchUserMessagesSuccessfullyWithStatus200ReturnsMessages() throws Exception {
        doReturn(Arrays.asList(message)).when(messagesService).getAllMessagesBySender(any(Long.class), any(Integer.class), any(Integer.class));

        mockMvc.perform(get("/messages")
                .contentType("application/json")
                .param("recipient", "1")
                .param("start", "1")
                .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages[0].id").value(message.getId()))
        ;
    }

    @Test
    void testSearchUserMessagesFailWithStatus404ReturnsUserNotFoundException() throws Exception {
        UserNotFoundException expectedException = new UserNotFoundException(String.format("The user with name %s does not exists",
                user.getUserName()));
        doThrow(expectedException).when(messagesService).getAllMessagesBySender(any(Long.class), any(Integer.class), any(Integer.class));

        mockMvc.perform(get("/messages")
                .contentType("application/json")
                .param("recipient", "1")
                .param("start", "1")
                .param("limit", "1"))
                .andExpect(jsonPath("$.error").value("User not found Exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("404"))
        ;
    }

    private MessageDto buildNewMessageDto() {
        MessageDto message = new MessageDto();
        message.setContent(new MessageContent(1L, MessageContentType.STRING, "abc"));
        message.setRecipient(1);
        message.setSender(1);
        return message;
    }

    //TODO: Test JWT security token

    private Message buildNewMessage() {
        Message message = new Message();
        message.setId(1L);
        message.setContent(new MessageContent(1L, MessageContentType.STRING, "abc"));
        message.setRecipient(buildNewUser());
        message.setSender(buildNewUser());
        message.setTimestamp(OffsetDateTime.now());
        return message;
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}
