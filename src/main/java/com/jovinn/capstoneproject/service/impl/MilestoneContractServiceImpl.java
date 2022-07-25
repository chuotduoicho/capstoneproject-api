package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.repository.MilestoneContractRepository;
import com.jovinn.capstoneproject.service.MilestoneContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MilestoneContractServiceImpl implements MilestoneContractService {

    @Autowired
    private MilestoneContractRepository milestoneContractRepository;
    @Override
    public void addMilestoneContract(MilestoneContract milestoneContract) {
        milestoneContractRepository.save(milestoneContract);
    }
}
