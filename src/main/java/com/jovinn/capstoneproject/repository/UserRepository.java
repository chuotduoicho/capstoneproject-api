package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findByUsername(@NotBlank String username);
    //Optional<User> findByEmail(@NotBlank String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    //User findByUsername(String username);
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
    Boolean existsByUsername(@NotBlank String username);

    Boolean existsByEmail(@NotBlank String email);
    Optional<User> findById(UUID id);
    User findUserByVerificationCode(String code);
}
