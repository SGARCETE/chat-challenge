package com.asapp.backend.challenge.model;

import com.asapp.backend.challenge.model.enums.MessageContentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "content")
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NotNull(message = "Please provide a valid type attribute in JSON request")
    @Enumerated(EnumType.STRING)
    MessageContentType type;
    @NotEmpty(message = "Please provide a text attribute in JSON request")
    String text;
}
