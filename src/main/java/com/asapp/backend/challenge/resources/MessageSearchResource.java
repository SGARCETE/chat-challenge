package com.asapp.backend.challenge.resources;

import com.asapp.backend.challenge.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageSearchResource {
    List<Message> messages;
}
