package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.PackageRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PackageController {
    @Autowired
    private PackageService packageService;
    @PostMapping("/package/{boxId}")
    public ResponseEntity<Package> addPackage(@PathVariable("boxId") UUID boxId, @Valid @RequestBody PackageRequest request,
                                              @CurrentUser UserPrincipal currentUser) {
        Package response = packageService.add(boxId, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/package/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable("id") UUID id, @Valid @RequestBody PackageRequest request,
                                                 @CurrentUser UserPrincipal currentUser) {
        Package response = packageService.update(id, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/package/{id}")
    public ResponseEntity<ApiResponse> deletePackage(@PathVariable("id") UUID id,
                                                     @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = packageService.delete(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
