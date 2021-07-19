package com.asapp.backend.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "content")
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class MessageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @NotEmpty(message = "Please provide a type attribute in JSON request")
    String type;
    @NotEmpty(message = "Please provide a text attribute in JSON request")
    String text;
}
