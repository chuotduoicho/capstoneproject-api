package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountServiceResponse;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.PackageRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.util.Pagination;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse saveBox(Box box, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản user"));

       if (seller.getUser().getId().equals(currentUser.getId())) {
           box.setSeller(seller);
           boxRepository.save(box);
           return new ApiResponse(Boolean.TRUE, "" + box.getId());
       }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public BoxResponse updateBox(Box box, UUID id, UserPrincipal currentUser) {
        Box boxExist = boxRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Not found box"));
        if (boxExist.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (box != null){
                boxExist.setDescription(box.getDescription());
                boxExist.setTitle(box.getTitle());
                boxExist.setStatus(box.getStatus());
                if (box.getPackages() != null) {
                    boxExist.setPackages(box.getPackages());
                }

                Box update = boxRepository.save(boxExist);
                return new BoxResponse(update.getId(), update.getCreateAt(), update.getUpdatedAt(), update.getSeller(),
                        update.getSeller().getId(), update.getTitle(), update.getDescription(), update.getImpression(),
                        update.getInteresting(), update.getStatus(), update.getSubCategory(), update.getPackages(), update.getGallery());
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Boolean deleteBox(UUID id) {
        try {
            boxRepository.deleteById(id);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Box updateStatus(UUID id) {
        return null;
    }

    //Dang loi :V
    @Override
    public List<Box> getListServiceBySellerId(UUID sellerId) {
        return boxRepository.findAllBySellerId(sellerId);
//        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
//        Page<Box> boxes = boxRepository.findAllBySellerId(sellerId, pageable);
//        List<BoxResponse> content = boxes.getContent().stream().map(
//                        box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
//        return new PageResponse<>(content, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
//                boxes.getTotalPages(), boxes.isLast());
    }

    @Override
    public List<Box> getAllService() {
        return boxRepository.findAll();
//        Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);
//        Page<Box> boxes = boxRepository.findAll(pageable);
//        List<Box> lists = boxes.getContent();
//        List<BoxResponse> content = lists.stream().map(
//                box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
//        return new PageResponse<>(content, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
//                boxes.getTotalPages(), boxes.isLast());
    }

    @Override
    public BoxResponse getServiceByID(UUID id) {
        Box box = boxRepository.findById(id)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy box"));
        return new BoxResponse(box.getId(), box.getCreateAt(), box.getUpdatedAt(), box.getSeller(),
                box.getSeller().getId(), box.getTitle(), box.getDescription(), box.getImpression(),
                box.getInteresting(), box.getStatus(), box.getSubCategory(), box.getPackages(), box.getGallery());
    }

    @Override
    public List<Box> getAllServiceByCategoryID(UUID categoryId) {
        return boxRepository.getAllServiceByCategoryId(categoryId) ;
//        Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);
//        Page<Box> boxes = boxRepository.getAllServiceByCategoryId(categoryId, pageable);
//        List<BoxResponse> content = boxes.getContent().stream().map(
//                        box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
//        return new PageResponse<>(content, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
//                boxes.getTotalPages(), boxes.isLast());
        //Pageable pageable = PageRequest.of(page,8, Sort.Direction.DESC);
    }

    @Override
    public Page<Box> getAllServiceByCatIdPagination(int page,UUID categoryId) {
        return boxRepository.findAllBySubCategory_Category_Id(categoryId,PageRequest.of(page,8));
    }

    @Override
    public Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String subCatName) {
        return boxRepository.findAllBySubCategory_NameContainsOrSubCategory_Category_NameContains(subCatName,catName,PageRequest.of(offset,8));
    }

    @Override
    public CountServiceResponse countTotalService() {
        return new CountServiceResponse(boxRepository.count());
    }

    @Override
    public CountServiceResponse countTotalServiceByCat(UUID catId) {
        return new CountServiceResponse(boxRepository.countBySubCategory_Category_Id(catId));
    }
}
