package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.PackageRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.PackageRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Override
    public Package savePackage(Package aPackage) {
        return packageRepository.save(aPackage);
    }

    @Override
    public List<Package> saveAllPackage(List<Package> packageList) {
        return packageRepository.saveAll(packageList);
    }

    @Override
    public Package add(UUID boxId, PackageRequest request, UserPrincipal currentUser) {
        Box box = boxRepository.findById(boxId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy box service"));
        if (box.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (box.getPackages().size() < 3) {
                Package pack = new Package();
                pack.setBox(box);
                pack.setTitle(request.getTitle());
                pack.setShortDescription(request.getShortDescription());
                pack.setPrice(request.getPrice());
                pack.setDeliveryTime(request.getDeliveryTime());
                if (pack.getContractCancelFee() == null) {
                    pack.setContractCancelFee(0);
                } else {
                    pack.setContractCancelFee(request.getContractCancelFee());
                }
                return packageRepository.save(pack);
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Chỉ được phép tạo tối đa 3 packges");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Package update(UUID id, PackageRequest request, UserPrincipal currentUser) {
        Package pack = packageRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy package"));
        if (pack.getBox().getSeller().getUser().getId().equals(currentUser.getId())) {
            pack.setTitle(request.getTitle());
            pack.setShortDescription(request.getShortDescription());
            pack.setPrice(request.getPrice());
            pack.setDeliveryTime(request.getDeliveryTime());
            if (pack.getContractCancelFee() == null) {
                pack.setContractCancelFee(0);
            } else {
                pack.setContractCancelFee(request.getContractCancelFee());
            }
            return packageRepository.save(pack);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Package pack = packageRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy package"));
        if (pack.getBox().getSeller().getUser().getId().equals(currentUser.getId())) {
            packageRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Xoá thành công package");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }
}
