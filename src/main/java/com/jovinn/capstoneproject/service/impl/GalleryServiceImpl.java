package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Gallery;
import com.jovinn.capstoneproject.repository.GalleryRespository;
import com.jovinn.capstoneproject.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRespository galleryRespository;

    @Override
    public Gallery saveGallery(Gallery gallery) {
        return galleryRespository.save(gallery);
    }
}
