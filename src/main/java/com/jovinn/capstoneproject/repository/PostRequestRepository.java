package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.PostRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRequestRepository extends JpaRepository<PostRequest, UUID> {
    List<PostRequest> findAllByUser_Id(UUID userId);
    List<PostRequest> findAllByCategory_Id(UUID categoryId);
    @Override
    Optional<PostRequest> findById(UUID uuid);
    PostRequest findPostRequestById(UUID postRequestId);
    Integer countPostRequestByUser_Id(UUID UserId);
}
