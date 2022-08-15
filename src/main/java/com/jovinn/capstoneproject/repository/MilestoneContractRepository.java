package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.MilestoneContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneContractRepository extends JpaRepository<MilestoneContract, UUID> {
    List<MilestoneContract> findAllByPostRequestId(UUID postRequestId);
    MilestoneContract deleteAllByPostRequestId(UUID postRequestId);
}
