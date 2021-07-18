package com.asapp.backend.challenge.services.impl;

import com.asapp.backend.challenge.exceptions.PasswordNotValidException;
import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    @Autowired
    private final UsersRepository usersRepository;

    public User authUser(String userName, String password) {
        User user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));

        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new PasswordNotValidException(String.format("Password not valid for user %s", userName));
        }
    }

}
