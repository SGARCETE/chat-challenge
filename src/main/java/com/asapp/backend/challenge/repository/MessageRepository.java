package com.asapp.backend.challenge.repository;

import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    List<Message> findAllBySender(User sender, Pageable pageable);
}
