package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    @Query("select u from Users u where u.userName = :username")
    Optional<Users> findByUserName(String username);

    @Query("select u from Users u where u.email = :email or u.userName = :email")
    Optional<Users> findByEmail(String email);

    @Query("select u from Users u where u.isActive = TRUE")
    List<Users> findByIsActive();

    @Query("select u from Users u where u.roleId.roleName = :userRole")
    Set<Users> findByUserRole(String userRole);

    Optional<Users> findByUserId(UUID userId);

    @Query("select count(u) from Users u")
    Integer getUserCount();
}
