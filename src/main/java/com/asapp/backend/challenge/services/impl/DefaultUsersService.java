package com.asapp.backend.challenge.services.impl;

import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUsersService implements UsersService {

    @Autowired
    private final UsersRepository usersRepository;

    public User createUser(User user) {
        return usersRepository.save(user);
    }

}
