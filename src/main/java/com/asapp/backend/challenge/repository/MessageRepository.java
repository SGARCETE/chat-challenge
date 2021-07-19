package com.asapp.backend.challenge.repository;

import com.asapp.backend.challenge.model.Message;
import com.asapp.backend.challenge.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message save(Message message);
    Page<Message> findAllByUser(User user, Pageable pageable);
}
