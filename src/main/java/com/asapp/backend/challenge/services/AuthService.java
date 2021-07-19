package com.asapp.backend.challenge.services;

import com.asapp.backend.challenge.model.User;

public interface AuthService {
    User authUser(String userName, String password);
    String getToken(String userName);
}
