package com.jovinn.capstoneproject.repo;

import com.jovinn.capstoneproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
