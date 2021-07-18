package com.asapp.backend.challenge.repository;

import com.asapp.backend.challenge.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    User save(User user);
}
