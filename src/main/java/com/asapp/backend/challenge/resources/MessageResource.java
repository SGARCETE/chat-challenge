package com.asapp.backend.challenge.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResource {
    long id;
    OffsetDateTime timestamp; //TODO: This timestamp should be in another format.
}
