package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.PackageRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface PackageService {
    //Add Package
    Package savePackage(Package aPackage);

    List<Package> saveAllPackage(List<Package> packageList);
    ApiResponse add(UUID boxId, PackageRequest request, UserPrincipal currentUser);
    ApiResponse update(UUID id, PackageRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
