package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.UrlProfile;

public interface UrlProfileService {
    UrlProfile saveUrlProfile(UrlProfile urlProfile);
    UrlProfile findBySellerId(Seller seller);
}
