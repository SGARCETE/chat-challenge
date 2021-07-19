package com.asapp.backend.challenge.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    @NotNull(message = "Please provide a sender attribute in JSON request")
    @Positive(message = "Please provide a valid sender attribute in JSON request")
    long sender;
    @NotNull(message = "Please provide a recipient attribute in JSON request")
    @Positive(message = "Please provide a valid recipient attribute in JSON request")
    long recipient;
    @NotEmpty(message = "Please provide a message attribute in JSON request")
    String message;
}
