package ru.kalimulin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kalimulin.models.Chat;
import ru.kalimulin.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUser1AndUser2(User user1, User user2);

    @Query("SELECT c FROM Chat c " +
            "WHERE c.user1 = :user OR c.user2 = :user")
    List<Chat> findByUser(@Param("user") User user);
}
