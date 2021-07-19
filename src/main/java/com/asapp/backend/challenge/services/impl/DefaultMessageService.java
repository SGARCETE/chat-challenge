package com.asapp.backend.challenge.services.impl;

import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMessageService implements MessageService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final MessageRepository messageRepository;

    public Message sendMessage(String message, long senderId, long recipientId) {
        User sender = usersRepository.findById(senderId).orElseThrow(()
                -> new UserNotFoundException(String.format("The user with ID %d does not exists", senderId)));
        User recipient = usersRepository.findById(recipientId).orElseThrow(()
                -> new UserNotFoundException(String.format("The user with ID %d does not exists", recipientId)));

        Message messageToSend = new Message(message, sender, recipient);
        return messageRepository.save(messageToSend);
    }

}
