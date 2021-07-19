package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.resources.AuthResource;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.services.AuthService;
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
@RequestMapping("/login")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResource> loginUser(@Valid @RequestBody User user){
        User response = authService.authUser(user.getUserName(), user.getPassword());
        return new ResponseEntity(new AuthResource(response.getId(), authService.getJWTToken(user.getUserName())), HttpStatus.OK);
    }

}
