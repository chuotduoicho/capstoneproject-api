package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.HistoryBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryBoxRepository extends JpaRepository<HistoryBox, UUID> {
    List<HistoryBox> findAllByUserIdOrderByCreateAtAsc(UUID userId);
    @Query("SELECT hb.boxId from HistoryBox hb WHERE hb.userId = :currentUserId")
    List<String> findBoxIdFromUser(@Param("currentUserId") UUID currentUserId);
}
