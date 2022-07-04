package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Gallery;
import com.jovinn.capstoneproject.repository.GalleryRepository;
import com.jovinn.capstoneproject.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    public Gallery saveGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }
}
