package com.jovinn.capstoneproject.service.impl;


import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.client.request.TargetSellerRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerApplyPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerTargetPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.PostRequestResponse;
import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.*;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostRequestServiceImpl implements PostRequestService {

    @Autowired
    private PostRequestRepository postRequestRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  SellerRepository sellerRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private MilestoneContractService milestoneContractService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    @Override
    public ApiResponse addPostRequest(PostRequestRequest request, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)){
            PostRequest postRequest = new PostRequest();
            postRequest.setCategory(categoryRepository.findCategoryById(request.getCategoryId()));
            postRequest.setSubCategory(subCategoryRepository.findSubCategoryById(request.getSubCategoryId()));
            postRequest.setRecruitLevel(request.getRecruitLevel());
            postRequest.setSkills(skillRepository.findAllByNameIn(request.getSkillsName()));
            postRequest.setJobTitle(request.getJobTitle());
            postRequest.setShortRequirement(request.getShortRequirement());
            postRequest.setAttachFile(request.getAttachFile());
            postRequest.setStatus(PostRequestStatus.OPEN);
            postRequest.setContractCancelFee(request.getContractCancelFee());
            List<MilestoneContract> milestoneContractList = request.getMilestoneContracts();
            BigDecimal budget = new BigDecimal(0);
            int totalDeliveryTime = 0;
            for (MilestoneContract milestoneContract : milestoneContractList){
                budget = budget.add(milestoneContract.getMilestoneFee());
                totalDeliveryTime = totalDeliveryTime + milestoneContract.getEndDate().getDate() - milestoneContract.getStartDate().getDate();
            }
            postRequest.setBudget(budget);
            postRequest.setTotalDeliveryTime(totalDeliveryTime);
            postRequest.setUser(userRepository.findUserById(currentUser.getId()));
            Notification notification;
            List<User> usersGetInvite = request.getInvitedUsers();
            for (User userInvite: usersGetInvite){
                notification = new Notification();
                notification.setUser(userInvite);
                notification.setUnread(Boolean.TRUE);
                notification.setLink(WebConstant.DOMAIN + "/sellerhome/manageRequest/" + postRequest.getId());
                notification.setShortContent("Bạn có lời mời làm việc mới từ " +
                        buyer.getUser().getFirstName() + " " + buyer.getUser().getLastName() +
                        " Kiểm tra ngay");
                notificationRepository.save(notification);
            }
            PostRequest savedPostRequest = postRequestRepository.save(postRequest);


            for (MilestoneContract milestoneContract : milestoneContractList){
                milestoneContract.setPostRequest(savedPostRequest);
                milestoneContractService.addMilestoneContract(milestoneContract);
            }
        }
        return new ApiResponse(Boolean.TRUE, "Khởi tạo yêu cầu thành công");
    }

    @Override
    public ApiResponse updatePostRequest(PostRequestRequest request, UUID id, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người mua"));
        PostRequest post = postRequestRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy bài đăng"));
        if (post.getUser().getId().equals(buyer.getUser().getId()) && post.getUser().getId().equals(currentUser.getId())){
            PostRequest savedPostRequest;
            if (request != null && post.getSellersApplyRequest().isEmpty() ){
                post.setCategory(categoryRepository.findCategoryById(request.getCategoryId()));
                post.setSubCategory(subCategoryRepository.findSubCategoryById(request.getSubCategoryId()));
                post.setRecruitLevel(request.getRecruitLevel());
                post.setSkills(skillRepository.findAllByNameIn(request.getSkillsName()));
                post.setJobTitle(request.getJobTitle());
                post.setShortRequirement(request.getShortRequirement());
                post.setAttachFile(request.getAttachFile());
                post.setStatus(PostRequestStatus.OPEN);
                post.setContractCancelFee(request.getContractCancelFee());
                List<MilestoneContract> milestoneContractList = request.getMilestoneContracts();
                BigDecimal budget = new BigDecimal(0) ;
                for (MilestoneContract milestoneContract : milestoneContractList){
                    budget = budget.add(milestoneContract.getMilestoneFee());
                }
                post.setBudget(budget);
                post.setUser(userRepository.findUserById(currentUser.getId()));
                savedPostRequest =  postRequestRepository.save(post);

                if (!milestoneContractList.isEmpty()){
                    for (MilestoneContract milestoneContract : milestoneContractList){
                        milestoneContract.setPostRequest(savedPostRequest);
                        milestoneContractService.addMilestoneContract(milestoneContract);
                    }
                }
                return new ApiResponse(Boolean.TRUE, "Cập nhật yêu cầu thành công");
            }
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Boolean deletePostRequest(UUID id) {
        postRequestRepository.deleteById(id);
        return true;
    }

    @Override
    public List<PostRequestResponse> getPostRequestByBuyerCreated(UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người mua"));
        List<PostRequest> postRequests;
       // PostRequestResponse postRequestResponse;
        List<PostRequestResponse> postRequestResponses = new ArrayList<>();
        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)) {
            postRequests = postRequestRepository.findAllByUser_Id(currentUser.getId());
            for (PostRequest postRequest : postRequests) {
                postRequestResponses.add(new PostRequestResponse(postRequest.getId(), postRequest.getCategory().getId(), postRequest.getSubCategory().getId(),
                        postRequest.getRecruitLevel(), postRequest.getSkills(), postRequest.getJobTitle(), postRequest.getShortRequirement(),
                        postRequest.getAttachFile(), postRequest.getMilestoneContracts(), postRequest.getContractCancelFee(), postRequest.getBudget(),
                        userService.getListUserInvitedByPostRequestId(postRequest.getId())));
            }
        }
        return postRequestResponses;
    }

    @Override
    public List<ListSellerTargetPostRequestResponse> getTargetSeller(TargetSellerRequest request) {
        List<String> boxes = sellerRepository.getTenSellerBySubCategoryId(request.getSubCategoryId(), request.getRankSeller(),
                                                                    request.getSkillName(), PageRequest.of(0,10));
        List<ListSellerTargetPostRequestResponse> responses = new ArrayList<>();
        for(String box : boxes) {
            Seller seller = sellerRepository.findById(UUID.fromString(box))
                    .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
            responses.add(new ListSellerTargetPostRequestResponse(seller.getUser().getId(), seller.getId(), seller.getUser().getAvatar(),
                    seller.getUser().getLastName() + " " + seller.getUser().getFirstName(), seller.getBrandName(),
                    seller.getTotalOrderFinish(), seller.getRatingPoint(), seller.getSkills().get(0).getName(),seller.getRankSeller()));
        }
        return responses;
    }

    @Override
    public List<PostRequestResponse> getPostRequestByCategoryId(UUID categoryId) {
        List<PostRequestResponse> postRequestResponses = new ArrayList<>();
        List<PostRequest> postRequests = postRequestRepository.findAllByCategory_Id(categoryId);
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);
        //Page<PostRequest> postRequests = postRequestRepository.findAllByCategory_Id(categoryId, pageable);
        //List<PostRequest> list = postRequests.getContent();

        for (PostRequest postRequest : postRequests){
            postRequestResponses.add(new PostRequestResponse(postRequest.getId(), postRequest.getCategory().getId(),
                    postRequest.getSubCategory().getId(), postRequest.getJobTitle(),postRequest.getBudget(),
                    postRequest.getUser().getBuyer().getId(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
                    postRequest.getUser().getCity(),postRequest.getCreateAt(), postRequest.getRecruitLevel(), postRequest.getSkills(),
                    postRequest.getShortRequirement(), postRequest.getMilestoneContracts(), postRequest.getContractCancelFee()));
        }

//        List<PostRequestResponse> content = list.stream().map(
//                        postRequest -> modelMapper.map(postRequest, PostRequestResponse.class))
//                        .collect(Collectors.toList());
//        return new PageResponse<>(content, postRequests.getNumber(), postRequests.getSize(), postRequests.getTotalElements(),
//                postRequests.getTotalPages(), postRequests.isLast());
        return postRequestResponses;
    }

    @Override
    public PostRequestResponse getPostRequestDetails(UUID postRequestId) {
        PostRequest postRequest = postRequestRepository.findPostRequestById(postRequestId);
        return new PostRequestResponse(postRequest.getId(), postRequest.getCategory().getId(),
                postRequest.getSubCategory().getId(), postRequest.getJobTitle(),postRequest.getBudget(),
                postRequest.getUser().getBuyer().getId(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
                postRequest.getUser().getCity(),postRequest.getCreateAt(), postRequest.getRecruitLevel(), postRequest.getSkills(),
                postRequest.getShortRequirement(), postRequest.getMilestoneContracts(), postRequest.getContractCancelFee());
//        return new PostRequestResponse(postRequest.getCategory().getName(),postRequest.getSubCategory().getName(),postRequest.getRecruitLevel(),
//                postRequest.getSkills(), postRequest.getJobTitle(), postRequest.getShortRequirement(), postRequest.getMilestoneContracts(),
//                postRequest.getContractCancelFee(), postRequest.getBudget(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
//                postRequest.getUser().getCity(),postRequest.getUser().getCreateAt(),postRequestRepository.countPostRequestByUser_Id(postRequest.getUser().getId()));
//        return postRequestRepository.findPostRequestById(postRequestId);
    }

    @Override
    public ApiResponse sellerApplyRequest(UUID postRequestId, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
        PostRequest post = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy bài đăng"));
        List<Seller> sellersApply;
        if (seller.getUser().getId().equals(currentUser.getId()) &&
                seller.getUser().getIsEnabled().equals(Boolean.TRUE)){

                sellersApply = sellerRepository.findAllByPostRequests_Id(postRequestId);
                for (Seller sellerApply: sellersApply){
                    if (sellerApply.getId().equals(seller.getId())){
                        return new ApiResponse(Boolean.FALSE, "Bạn đã ứng cử vào bài đăng này");
                    }
                }
                sellersApply.add(seller);
                post.setSellersApplyRequest(sellersApply);
                postRequestRepository.save(post);

            return new ApiResponse(Boolean.TRUE,"Ứng cử thành công");

        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ListSellerApplyPostRequestResponse getListSellerApply(UUID postRequestId, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy bài đăng"));
        //Pageable pageable = Pagination.paginationCommon(page, size, "sellerId", sortDir);

//        List<Seller> sellers = postRequest.getSellersApplyRequest();
        if (postRequest.getUser().getId().equals(currentUser.getId())) {
//            Page<Seller> sellers = sellerRepository.findAllByPostRequestsId(postRequestId, pageable);
//            List<Seller> list = sellers.getContent();
//            List<ListSellerApplyPostRequestResponse> content = list.stream().map(
//                            sellerApplyPostRequest -> modelMapper.map(sellerApplyPostRequest, ListSellerApplyPostRequestResponse.class))
//                            .collect(Collectors.toList());
//            return new PageResponse<>(content, sellers.getNumber(), sellers.getSize(), sellers.getTotalElements(),
//                    sellers.getTotalPages(), sellers.isLast());
            return new ListSellerApplyPostRequestResponse(postRequest.getId(), postRequest.getSellersApplyRequest());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<PostRequestResponse> getAllPostRequest() {
        List<PostRequestResponse> postRequestResponses = new ArrayList<>();
        List<PostRequest> postRequests = postRequestRepository.findAll();
        for (PostRequest postRequest:postRequests){
            postRequestResponses.add(new PostRequestResponse(postRequest.getId(), postRequest.getCategory().getId(),
                    postRequest.getSubCategory().getId(), postRequest.getJobTitle(),postRequest.getBudget(),
                    postRequest.getUser().getBuyer().getId(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
                    postRequest.getUser().getCity(),postRequest.getCreateAt(), postRequest.getRecruitLevel(), postRequest.getSkills(),
                    postRequest.getShortRequirement(), postRequest.getMilestoneContracts(), postRequest.getContractCancelFee()));
        }
        return postRequestResponses;
    }

    @Override
    public CountPostRequestResponse countTotalPostRequestByCatId(UUID catId) {
        return new CountPostRequestResponse(postRequestRepository.countPostRequestByCategory_Id(catId));
    }
}
