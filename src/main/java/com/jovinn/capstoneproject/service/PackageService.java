package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Package;

import java.util.List;

public interface PackageService {
    //Add Package
    Package savePackage(Package aPackage);

    List<Package> saveAllPackage(List<Package> packageList);
}
