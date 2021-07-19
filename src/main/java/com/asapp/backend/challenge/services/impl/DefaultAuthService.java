package com.asapp.backend.challenge.services.impl;

import com.asapp.backend.challenge.exceptions.PasswordNotValidException;
import com.asapp.backend.challenge.exceptions.UserNotFoundException;
import com.asapp.backend.challenge.model.User;
import com.asapp.backend.challenge.repository.UsersRepository;
import com.asapp.backend.challenge.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getJWTToken(String userName) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("ChallengeJWT")
                .setSubject(userName)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

}
