package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.model.Message;

public interface MessageService {
    Message sendMessage(String message, long senderId, long recipientId);
}
