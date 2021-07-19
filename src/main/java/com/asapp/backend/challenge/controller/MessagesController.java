package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.dtos.MessageDto;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.resources.MessageSearchResource;
import com.asapp.backend.challenge.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResource> sendMessage(@Valid @RequestBody MessageDto messageDto) {
        Message response = messageService.sendMessage(messageDto.getContent()
                , messageDto.getSender(), messageDto.getRecipient());
        return new ResponseEntity(new MessageResource(response.getId(), response.getTimestamp()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<MessageResource> searchMessages(@RequestParam @Min(1) @Positive(message = "Please provide a valid recipient attribute in JSON request") long recipient,
                                                          @RequestParam @Min(1) @Positive(message = "Please provide a valid start attribute in JSON request") Integer start,
                                                          @RequestParam(required = false) Integer limit) {
        if(limit == null) {
            limit = 100;
        }

        List<Message> response = messageService.getAllMessagesBySender(recipient, start, limit);
        return new ResponseEntity(new MessageSearchResource(response), HttpStatus.OK);
    }

}
