package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.MessageContent;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.model.enums.MessageContentType;
import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.impl.DefaultMessageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultMessageServiceTest {
    private MessageService messageService;
    private UsersRepository userRepository;
    private MessageRepository messageRepository;
    private User sender;
    private User recipient;
    private Message message;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        messageRepository = Mockito.mock(MessageRepository.class);
        messageService = new DefaultMessageService(userRepository, messageRepository);
        sender = buildNewUser();
        recipient = buildNewUser().setId(2L);
        message = buildNewMessage(sender, recipient);
    }

    @Test
    public void testSendMessageThenReturnId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(sender));
        Mockito.when(messageRepository.save(Mockito.any(Message.class))).thenReturn(message);

        Message response = messageService.sendMessage(message.getContent(), sender.getId(), recipient.getId());

        assertEquals(message.getId(), response.getId());
    }

    @Test
    public void testSendMessageThenThrowsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        Throwable ex = assertThrows(UserNotFoundException.class, () -> {messageService.sendMessage(message.getContent(), sender.getId(), recipient.getId());});
        assertEquals(String.format("The user with ID %s does not exists", sender.getId()), ex.getMessage());
    }

    @Test
    public void testSearchUserMessagesThenReturnMessages() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(sender));
        Mockito.when(messageRepository.findAllBySender(Mockito.any(User.class), Mockito.any(Pageable.class))).thenReturn(Arrays.asList(message));

        List<Message> response = messageService.getAllMessagesBySender(sender.getId(), 1, 10);

        assertEquals(1, response.size());
        assertEquals(message.getId(), response.get(0).getId());
    }

    @Test
    public void testSearchUserMessagesThenThrowsUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Throwable ex = assertThrows(UserNotFoundException.class, () -> {messageService.getAllMessagesBySender(sender.getId(), 1, 10);});
        assertEquals(String.format("The user with ID %s does not exists", sender.getId()), ex.getMessage());
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

    private Message buildNewMessage(User sender, User recipient) {
        Message message = new Message();
        message.setId(1L);
        message.setContent(new MessageContent(1L, MessageContentType.STRING, "abc"));
        message.setRecipient(sender);
        message.setSender(recipient);
        message.setTimestamp(OffsetDateTime.now());
        return message;
    }
}
