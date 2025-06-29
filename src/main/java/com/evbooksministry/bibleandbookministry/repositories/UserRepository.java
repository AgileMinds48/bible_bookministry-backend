package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("select u from Users u where u.userName = :username")
    Optional<Users> findByUserName(String username);
}
