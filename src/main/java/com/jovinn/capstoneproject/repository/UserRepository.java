package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;
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
    User findUserByVerificationCode(String code);
    User findUserById(UUID id);

    List<User> findUserByPostRequests_Id(UUID postRequestId);
    @Query("SELECT u FROM User u WHERE u.phoneNumber = ?1")
    User findUserByPhoneNumber(String phoneNumber);
}
