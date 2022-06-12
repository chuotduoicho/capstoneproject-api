package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.PackageRepository;
import com.jovinn.capstoneproject.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageRepository packageRepository;
    @Override
    public Package savePackage(Package aPackage) {
        return packageRepository.save(aPackage);
    }

    @Override
    public List<Package> saveAllPackage(List<Package> packageList) {
        return packageRepository.saveAll(packageList);
    }
}
