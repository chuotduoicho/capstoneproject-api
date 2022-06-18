package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Certificate;
import com.jovinn.capstoneproject.repository.CertificateRepository;
import com.jovinn.capstoneproject.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Certificate saveCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }
}
