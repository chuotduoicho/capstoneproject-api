package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByContractIdOrderByCreateAtDesc(UUID contractId);
}
