package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.MessageContent;

import java.util.List;

public interface MessagesService {
    Message sendMessage(MessageContent message, long senderId, long recipientId);
    List<Message> getAllMessagesBySender(Long senderId, Integer start, Integer limit);
}
