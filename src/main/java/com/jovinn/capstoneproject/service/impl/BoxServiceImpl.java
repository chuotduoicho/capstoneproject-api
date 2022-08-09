package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountServiceResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchRequest;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.ListBoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.request.BoxRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.BoxResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.BadRequestException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Gallery;
import com.jovinn.capstoneproject.model.HistoryBox;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.GalleryRepository;
import com.jovinn.capstoneproject.repository.HistoryBoxRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.thirdapi.GoogleDriveManagerService;
import com.jovinn.capstoneproject.util.Pagination;
import com.jovinn.capstoneproject.util.SpecificationSearchBox;
import com.jovinn.capstoneproject.util.WebConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private HistoryBoxRepository historyBoxRepository;
    @Autowired
    private GoogleDriveManagerService googleDriveManagerService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse addBox(Box box, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản user"));

        if (seller.getUser().getId().equals(currentUser.getId())) {
            List<Box> boxes = boxRepository.findAllBySellerId(seller.getId());
            if(boxes.size() >= 5 && seller.getRankSeller().equals(RankSeller.BEGINNER)) {
                return new ApiResponse(Boolean.FALSE, "Bạn chỉ được tạo tối đa 5 hộp dịch vụ do chưa đạt cấp độ người bán cao hơn");
            } else {
                box.setSeller(seller);
                box.setImpression(0);
                box.setInteresting(0);
                BigDecimal fromPrice = box.getPackages().get(0).getPrice();
                box.setFromPrice(fromPrice);
                boxRepository.save(box);
                return new ApiResponse(Boolean.TRUE, "" + box.getId());
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse updateBox(UUID id, BoxRequest request, UserPrincipal currentUser) {
        Box box = checkExistBox(id);
        if (box.getSeller().getUser().getId().equals(currentUser.getId())) {
            try {
                box.setDescription(request.getDescription());
                box.setTitle(request.getTitle());
                Gallery gallery = box.getGallery();
                updateGallery(gallery, request);
                boxRepository.save(box);
                removeOldFile(gallery);
                return new ApiResponse(Boolean.TRUE, "Cập nhật hộp dịch vụ thành công");
            } catch (BadRequestException e) {
                return new ApiResponse(Boolean.FALSE, "Cập nhật hộp dịch vụ thất bại");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deleteBox(UUID id, UserPrincipal currentUser) {
        Box box = checkExistBox(id);
        if(box.getSeller().getUser().getId().equals(currentUser.getId())) {
            try {
                Gallery gallery = box.getGallery();
                removeOldFile(gallery);
                boxRepository.deleteById(id);
                return new ApiResponse(Boolean.TRUE, "Xóa hộp dịch vụ thành công");
            } catch (Exception e){
                return new ApiResponse(Boolean.FALSE, "Xóa hộp dịch vụ thất bại");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse updateStatus(UUID id, UserPrincipal currentUser) {
        Box box = checkExistBox(id);
        if(box.getSeller().getUser().getId().equals(currentUser.getId())) {
            if(box.getStatus().equals(BoxServiceStatus.ACTIVE)) {
                box.setStatus(BoxServiceStatus.DEACTIVE);
            } else {
                box.setStatus(BoxServiceStatus.ACTIVE);
            }
            boxRepository.save(box);
            return new ApiResponse(Boolean.TRUE, "Đã thay đổi trạng thái hộp dịch vụ thành công");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PageResponse<BoxSearchResponse> getListServiceBySellerId(UUID sellerId, UserPrincipal currentUser,
                                                              BoxServiceStatus status, int page, int size) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy tài khoản user"));

        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
        Page<Box> boxes;
        if(seller.getUser().getId().equals(currentUser.getId())) {
            boxes = boxRepository.findAllBySellerIdAndStatus(sellerId, status, pageable);
        } else {
            boxes = boxRepository.findAllBySellerIdAndStatus(sellerId, BoxServiceStatus.ACTIVE, pageable);
        }

        String message = boxes.getNumberOfElements() != 0 ?
                "Bạn có " + boxes.getTotalElements() + "/5 dịch vụ"
                : WebConstant.NOT_FOUND_BOX;

        List<BoxSearchResponse> content = boxes.getContent().stream().map(
                        box -> modelMapper.map(box, BoxSearchResponse.class))
                        .collect(Collectors.toList());

        return new PageResponse<>(content, message, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
                boxes.getTotalPages(), boxes.isLast());
    }

    @Override
    public List<Box> getAllService() {
        return boxRepository.findAll();
    }

    @Override
    public BoxResponse getServiceByID(UUID id, UserPrincipal currentUser) {
        Box box = checkExistBox(id);
        createHistoryBox(id, currentUser.getId());
        if(!box.getSeller().getUser().getId().equals(currentUser.getId())) {
            box.setImpression(box.getImpression() + 1);
            Box save = boxRepository.save(box);
            return boxResponseConfig(save);
        }
        return boxResponseConfig(box);
    }

    @Override
    public BoxResponse getBoxByIdForGuest(UUID id) {
        Box box = checkExistBox(id);
        return boxResponseConfig(box);
    }

    @Override
    public List<Box> getAllServiceByCategoryID(UUID categoryId) {
        return boxRepository.getAllServiceByCategoryId(categoryId);
    }

    @Override
    public Page<Box> getAllServiceByCatIdPagination(int page, UUID categoryId) {
        return boxRepository.findAllBySubCategory_Category_Id(categoryId, PageRequest.of(page,8));
    }

    @Override
    public Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String subCatName) {
        return boxRepository.findAllBySubCategory_NameContainsOrSubCategory_Category_NameContains(subCatName, catName, PageRequest.of(offset,8));
    }

    @Override
    public CountServiceResponse countTotalService() {
        return new CountServiceResponse(boxRepository.count());
    }

    @Override
    public CountServiceResponse countTotalServiceByCat(UUID catId) {
        return new CountServiceResponse(boxRepository.countBySubCategory_Category_Id(catId));
    }

    @Override
    public PageResponse<BoxSearchResponse> searchResult(String searchKeyWord, UUID categoryId, UUID subCategoryId,
                                                        BigDecimal minPrice, BigDecimal maxPrice,
                                                        int ratingPoint, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);
        Page<Box> boxes;

        if(subCategoryId != null && minPrice != null && maxPrice != null) {
            boxes = boxRepository.findAllBySubCategoryIdAndFromPriceBetweenAndTitleLike(subCategoryId, minPrice, maxPrice, searchKeyWord, BoxServiceStatus.ACTIVE, pageable);
        } else if(subCategoryId != null){
            boxes = boxRepository.findAllBySubCategoryAndSearchKeyWord(subCategoryId, searchKeyWord, BoxServiceStatus.ACTIVE, pageable);
        } else if(minPrice != null && maxPrice != null){
            boxes = boxRepository.findAllByFromPriceAndSearchKeyWord(minPrice, maxPrice, searchKeyWord, BoxServiceStatus.ACTIVE, pageable);
        } else {
            boxes = boxRepository.findAllByTitleLike(searchKeyWord, BoxServiceStatus.ACTIVE, pageable);
        }

        List<Box> list = boxes.getNumberOfElements() == 0 ? Collections.emptyList() : boxes.getContent();
        String message = boxes.getNumberOfElements() != 0 ?
                "Tìm thấy " + boxes.getTotalElements() + " dịch vụ với từ khóa " + "< " +  searchKeyWord + " >"
                : WebConstant.NOT_FOUND_BOX + " với từ khóa " + "< " + searchKeyWord + " >";

        List<BoxSearchResponse> content = list.stream().map(
                        box -> modelMapper.map(box, BoxSearchResponse.class))
                        .collect(Collectors.toList());

        return new PageResponse<>(content, message, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
                boxes.getTotalPages(), boxes.isLast());
    }

    @Override
    public List<BoxSearchResponse> getListHistoryBox(UserPrincipal currentUser) {
        List<String> boxIds = historyBoxRepository.findBoxIdFromUser(currentUser.getId());
        List<BoxSearchResponse> responses = new ArrayList<>();
        for(String boxId : boxIds) {
            Box box = checkExistBox(UUID.fromString(boxId));
            responses.add(new BoxSearchResponse(box.getId(), box.getCreateAt(), box.getUpdatedAt(),
                    box.getSeller(), box.getGallery().getImageGallery1(), box.getSeller().getUser().getAvatar(),
                    box.getSeller().getBrandName(), box.getSeller().getRankSeller(), box.getSeller().getTotalOrderFinish(),
                    box.getSeller().getRatingPoint(), box.getImpression(), box.getTitle(), box.getFromPrice()));
        }
        return responses;
    }

    @Override
    public List<BoxSearchResponse> getTop8BoxByImpression() {
        List<Box> boxes = boxRepository.getTop8ByImpression(BoxServiceStatus.ACTIVE, PageRequest.of(0, 8));
        return boxes.stream().map(
                    box -> modelMapper.map(box, BoxSearchResponse.class))
                    .collect(Collectors.toList());
    }

    @Override
    public List<BoxSearchResponse> getTop8BoxByCategoryOrderByImpression(UUID categoryId) {
        List<Box> boxes = boxRepository.getTop8BoxByCategoryOrderByImpression(BoxServiceStatus.ACTIVE, categoryId, PageRequest.of(0, 8));
        return boxes.stream().map(
                    box -> modelMapper.map(box, BoxSearchResponse.class))
                    .collect(Collectors.toList());
    }

    @Override
    public PageResponse<BoxSearchResponse> getBoxes(UUID categoryId, UUID subCategoryId,
                                              BigDecimal minPrice, BigDecimal maxPrice, int ratingPoint,
                                              String searchKeyWord, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);

        Page<Box> boxes;
        if(subCategoryId != null) {
            boxes = boxRepository.findAllBySubCategoryIdAndFromPriceBetweenAndTitleLike(subCategoryId, minPrice, maxPrice, searchKeyWord, BoxServiceStatus.ACTIVE, pageable);
            //boxes = boxRepository.findAllBySubCategoryIdAndFromPriceBetweenAndTitleLike(subCategoryId, minPrice, maxPrice, searchKeyWord, pageable);

            //boxes = boxRepository.getListByParamsFilter(subCategoryId, minPrice, maxPrice, pageable);
//            boxes = boxRepository.getBoxesByFilter(subCategoryId, minPrice, maxPrice,
//                    ratingPoint, searchKeyWord, pageable);
        } else {
            boxes = boxRepository.getAllServiceByCategoryId(categoryId, pageable);
        }

        List<Box> list = boxes.getNumberOfElements() == 0 ? Collections.emptyList() : boxes.getContent();
        List<BoxSearchResponse> content = new ArrayList<>();
        String message = boxes.getNumberOfElements() != 0 ? "Tìm thấy " + boxes.getTotalElements() + " dịch vụ" : WebConstant.NOT_FOUND_BOX;

        if(list.isEmpty()) {
            return new PageResponse<>(content, message, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
                    boxes.getTotalPages(), boxes.isLast());
        } else {
            content = list.stream().map(
                    box -> modelMapper.map(box, BoxSearchResponse.class))
                    .collect(Collectors.toList());
            return new PageResponse<>(content, message, boxes.getNumber(), boxes.getSize(), boxes.getTotalElements(),
                    boxes.getTotalPages(), boxes.isLast());
        }
    }

    @Override
    public ListBoxSearchResponse search(BoxSearchRequest request) {
        Specification<Box> specification;
        Specification<Box> byPrice = null;
        UUID categoryId = request.getCategoryId();
        UUID subCategoryId = request.getSubCategoryId();
        String searchKeyWord = request.getSearchKeyWord();
        BigDecimal maxPrice = request.getMaxPrice();

        if(request.getMinPrice() != null && request.getMaxPrice() != null) {
            byPrice = SpecificationSearchBox.getPrice(request);
        }

        if(subCategoryId == null) {
            specification = SpecificationSearchBox.getAllActiveBoxWithCategory(request).and(byPrice);
        } else {
            specification = SpecificationSearchBox.getAllActiveBoxWithSubCategory(request).and(byPrice);
        }

        var boxPage = boxRepository.findAll(specification, getPageRequest(request));
        return responseList(boxPage);
    }


    private PageRequest getPageRequest(BoxSearchRequest request) {
        return PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortDirection(), "impression"));
    }

    private ListBoxSearchResponse responseList(Page<Box> request) {
        return new ListBoxSearchResponse(
                request.getContent().stream().map(
                        box -> modelMapper.map(box, BoxSearchResponse.class))
                        .collect(Collectors.toList()),
                request.getTotalElements() != 0 ? "Tìm thấy " + request.getTotalElements() + " dịch vụ" : WebConstant.NOT_FOUND_BOX,
                request.getPageable().getPageNumber(),
                request.getPageable().getPageSize(),
                request.getTotalElements(), request.getTotalPages(),
                request.isLast()
        );
    }

    private Box checkExistBox(UUID boxId) {
        return boxRepository.findById(boxId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hộp dịch vụ"));
    }

    private BoxResponse boxResponseConfig(Box box) {
        return new BoxResponse(box.getId(), box.getCreateAt(), box.getUpdatedAt(), box.getSeller(),
                box.getSeller().getId(), box.getTitle(), box.getDescription(), box.getImpression(),
                box.getInteresting(), box.getStatus(), box.getSubCategory(), box.getPackages(), box.getGallery());
    }

    private void updateGallery(Gallery gallery, BoxRequest request) {
        if(request.getImageGallery1() != null) {
            gallery.setImageGallery1(request.getImageGallery1());
        }
        if(request.getImageGallery2() != null) {
            gallery.setImageGallery1(request.getImageGallery2());
        }
        if(request.getImageGallery3() != null) {
            gallery.setImageGallery1(request.getImageGallery3());
        }
        if(request.getVideoGallery() != null) {
            gallery.setVideoGallery(request.getVideoGallery());
        }
        if(request.getDocumentGallery() != null) {
            gallery.setDocumentGallery(request.getDocumentGallery());
        }
        galleryRepository.save(gallery);
    }

    private void removeOldFile(Gallery gallery) {
        deleteOldFileUploadOnDrive(gallery.getImageGallery1());
        deleteOldFileUploadOnDrive(gallery.getImageGallery2());
        deleteOldFileUploadOnDrive(gallery.getImageGallery3());
        deleteOldFileUploadOnDrive(gallery.getVideoGallery());
        deleteOldFileUploadOnDrive(gallery.getDocumentGallery());
    }

    private void deleteOldFileUploadOnDrive(String fileUrl) {
        googleDriveManagerService.deleteFile(fileUrl.substring(31));
    }

    private void createHistoryBox(UUID boxId, UUID userId) {
        HistoryBox historyBox = new HistoryBox();
        historyBox.setBoxId(boxId);
        historyBox.setUserId(userId);
        List<HistoryBox> listHistory = historyBoxRepository.findAllByUserIdOrderByCreateAtAsc(userId);
        if(listHistory.size() >= 10) {
            historyBoxRepository.deleteById(listHistory.get(0).getId());
        }
        historyBoxRepository.save(historyBox);
    }
}
