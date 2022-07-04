package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.CategoryRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Box saveBox(Box box, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản user"));

       if (seller.getUser().getId().equals(currentUser.getId())) {
           box.setSeller(seller);
           return boxRepository.save(box);
       }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Box updateBox(Box box, UUID id) {
        if (box != null){
            Box boxExist = boxRepository.getReferenceById(id);
            if (boxExist != null){
                if (box.getDescription() != null) {
                    boxExist.setDescription(box.getDescription());
                }
                if (box.getImpression() != null) {
                    boxExist.setImpression(box.getImpression());
                }
                if (box.getInteresting() != null){
                    boxExist.setInteresting(box.getInteresting());
                }
                if (box.getSeller() != null){
                    boxExist.setSeller(box.getSeller());
                }
                if(box.getSubCategory() != null){
                    boxExist.setSubCategory(box.getSubCategory());
                }


                return boxRepository.save(boxExist);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteBox(UUID id) {
        boxRepository.deleteById(id);
        return true;
    }

    @Override
    public Box updateStatus(UUID id) {
        return null;
    }

    //Dang loi :V
    @Override
    public List<Box> getListServiceBySellerId(UUID sellerId) {
        return boxRepository.findAllById(sellerId);
    }

    @Override
    public List<Box> getAllService() {
        return boxRepository.findAll();
    }

    @Override
    public Box getServiceByID(UUID id) {
        return boxRepository.findById(id).orElse(null);
    }

    @Override
    public List<Box> getAllServiceByCategoryID(UUID categoryId) {
        //Pageable pageable = PageRequest.of(page,8, Sort.Direction.DESC);
        return boxRepository.getAllServiceByCategoryId(categoryId) ;
    }

    @Override
    public Page<Box> getAllServiceByCatIdPagination(int page,UUID categoryId) {
        return boxRepository.findAllBySubCategory_Category_Id(categoryId,PageRequest.of(page,8));
    }

    @Override
    public Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String subCatName) {
        return boxRepository.findAllBySubCategory_NameContainsOrSubCategory_Category_NameContains(subCatName,catName,PageRequest.of(offset,8));
    }
}
