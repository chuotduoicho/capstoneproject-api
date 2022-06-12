package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public Box saveBox(Box box) {
        if (box != null){
            return boxRepository.save(box);
        }
        return null;
    }

    @Override
    public Box updateBox(Box box, UUID id) {
        if (box != null){
            Box boxExist = boxRepository.getReferenceById(id);
            if (boxExist != null){
                if (box.getDescription() != null) {
                    boxExist.setDescription(box.getDescription());
                }
                if (box.getImpression() != null) {
                    boxExist.setImpression(box.getImpression());
                }
                if (box.getInteresting() != null){
                    boxExist.setInteresting(box.getInteresting());
                }
                if (box.getSellerId() != null){
                    boxExist.setSellerId(box.getSellerId());
                }

                return boxRepository.save(boxExist);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteBox(UUID id) {
        boxRepository.deleteById(id);
        return true;
    }

    @Override
    public Box updateStatus(UUID id) {
        return null;
    }

    //Dang loi :V
    @Override
    public List<Box> getListServiceBySellerId(UUID sellerId) {
        return boxRepository.findAllById(sellerId);
    }

    @Override
    public List<Box> getAllService() {
        return boxRepository.findAll();
    }

    @Override
    public Box getServiceByID(UUID id) {
        return boxRepository.findById(id).orElse(null);
    }
}
