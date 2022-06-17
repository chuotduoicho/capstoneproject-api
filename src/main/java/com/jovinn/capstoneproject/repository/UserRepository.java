package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    //Optional<User> findByUserName(String username);
    Optional<User> findUserByEmail(String email);
    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findById(UUID id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
    default User getUser(UserPrincipal currentUser) {
        return getUserByName(currentUser.getName());
    }

    default User getUserByName(String username) {
        return findByUsername(username);
    }
}
