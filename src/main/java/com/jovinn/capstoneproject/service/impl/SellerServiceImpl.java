package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.CertificateRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CertificateRepository certificateRepository;
    @Override
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public List<Seller> getListInfoBySellerId(UUID sellerId) {
        return null;
    }

    @Override
    public Seller getSellerById(UUID id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "seller not found", id ));
    }

    @Override
    public Seller getSellerBySellerNumber(String sellerNumber) {
        return sellerRepository.findBySellerNumber(sellerNumber);
    }

    @Override
    public void updateSeller(UUID id, Seller seller) {
        Seller existSeller = sellerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller Not found by ", id));
        existSeller.setDescriptionBio(seller.getDescriptionBio());
//        Certificate certificate = certificateRepository.findBySellerId(id);
//        certificate.setSeller(seller);
//        existSeller.setCertificates(seller.getCertificates());
//        existSeller.setUrlProfiles(seller.getUrlProfiles());
//        existSeller.setEducations(seller.getEducations());
//        existSeller.setLanguages(seller.getLanguages());
//        existSeller.setSkills(seller.getSkills());

        sellerRepository.save(existSeller);
    }

    @Override
    public Seller deleteSeller(UUID id) {
        return null;
    }

//    @Override
//    public Seller updateSeller(Seller seller) {
//        Seller existSeller = sellerRepository.findById(seller.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("seller", "seller not exist", seller));
//        existSeller.setDescriptionBio(seller.getDescriptionBio());
//        return sellerRepository.save(seller);
//    }
}
