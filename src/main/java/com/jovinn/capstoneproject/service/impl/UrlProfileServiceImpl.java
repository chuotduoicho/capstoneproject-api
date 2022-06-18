package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.UrlProfile;
import com.jovinn.capstoneproject.repository.UrlProfileRepository;
import com.jovinn.capstoneproject.service.UrlProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlProfileServiceImpl implements UrlProfileService {
    @Autowired
    private UrlProfileRepository urlProfileRepository;
    @Override
    public UrlProfile saveUrlProfile(UrlProfile urlProfile) {
        return urlProfileRepository.save(urlProfile);
    }

    @Override
    public UrlProfile findBySellerId(Seller seller) {
        return null;
    }
}
