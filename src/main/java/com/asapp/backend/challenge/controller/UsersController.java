package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.services.UsersService;
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
@RequestMapping("/users")
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) { //TODO: User must be a DTO instead of domain user
        User response = usersService.createUser(user);
        LOGGER.debug(String.format("User with id %s created succesfully", response.getId()));
        return new ResponseEntity(new UserResource(response.getId()), HttpStatus.OK);
    }

}
