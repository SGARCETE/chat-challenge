package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.MessageDto;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<User> createMessage(@Valid @RequestBody MessageDto messageDto) {
        Message response = messageService.sendMessage(messageDto.getMessage()
                , messageDto.getSender(), messageDto.getRecipient());
        return new ResponseEntity(new MessageResource(response.getId(), response.getDateCreated()), HttpStatus.OK);
    }
}
