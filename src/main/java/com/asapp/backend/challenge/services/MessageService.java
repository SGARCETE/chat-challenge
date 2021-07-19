package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.model.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(String message, long senderId, long recipientId);
    List<Message> getAllMessagesBySender(Long senderId, Integer start, Integer limit);
}
