package com.asapp.backend.challenge.services.impl;

import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.MessageContent;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMessagesService implements MessagesService {

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final MessageRepository messageRepository;

    public Message sendMessage(MessageContent message, long senderId, long recipientId) {
        User sender = usersRepository.findById(senderId).orElseThrow(()
                -> new UserNotFoundException(String.format("The user with ID %d does not exists", senderId)));
        User recipient = usersRepository.findById(recipientId).orElseThrow(()
                -> new UserNotFoundException(String.format("The user with ID %d does not exists", recipientId)));

        Message messageToSend = new Message(message, sender, recipient);
        return messageRepository.save(messageToSend);
    }

    public List<Message> getAllMessagesBySender(Long senderId, @Positive(message = "Please provide a valid limit attribute in JSON request") Integer start,  @Positive(message = "Please provide a valid limit attribute in JSON request") Integer limit) {
        User sender = usersRepository.findById(senderId).orElseThrow(()
                -> new UserNotFoundException(String.format("The user with ID %d does not exists", senderId)));
        Pageable size = PageRequest.of(0,limit);
        List<Message> messages = messageRepository.findAllBySender(sender, size);
        return getMessagesWithStart(messages, start);
    }

    private List<Message> getMessagesWithStart(List<Message> messages, Integer start) {
        if(start > messages.size()) {
            return messages;
        } else {
            return messages.subList(start - 1, messages.size());
        }
    }

}
