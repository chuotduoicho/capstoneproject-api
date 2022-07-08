package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.PostRequestResponse;
import com.jovinn.capstoneproject.enumerable.PostRequestStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.MilestoneContractService;
import com.jovinn.capstoneproject.service.NotificationService;
import com.jovinn.capstoneproject.service.PostRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    private MilestoneContractService milestoneContractService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public ApiResponse addPostRequest(PostRequestRequest request, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)){
//            milestoneContractRepository.saveAll(request.getMilestoneContracts());
            PostRequest postRequest = new PostRequest();
            PostRequest savedPostRequest;
            postRequest.setCategory(categoryRepository.findCategoryById(request.getCategoryId()));
            postRequest.setSubCategory(subCategoryRepository.findSubCategoryById(request.getSubcategoryId()));
            postRequest.setRecruitLevel(request.getRecruitLevel());
            postRequest.setSkills(skillRepository.findAllByNameIn(request.getSkillsName()));
            postRequest.setJobTitle(request.getJobTitle());
            postRequest.setShortRequirement(request.getShortRequirement());
            postRequest.setAttachFile(request.getAttachFile());
            postRequest.setStatus(PostRequestStatus.OPEN);
            postRequest.setContractCancelFee(request.getContractCancelFee());
            postRequest.setBudget(request.getBudget());
            postRequest.setUser(userRepository.findUserById(currentUser.getId()));
            Notification notification;
            List<User> usersGetInvite = request.getInvitedUsers();
            for (User userInvite: usersGetInvite){
                notification = new Notification();
                notification.setUser(userInvite);
                notification.setLink("/getPostRequestDetails/"+buyer.getUser().getId().toString()+"");
                notification.setShortContent("You have new invite from "+buyer.getUser().getFirstName()+" "+buyer.getUser().getLastName()+"");
                notificationService.saveNotification(notification);
            }
            savedPostRequest =  postRequestRepository.save(postRequest);
            List<MilestoneContract> milestoneContractList = request.getMilestoneContracts();
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
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        PostRequest post = postRequestRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Post Request not found "));
        if (post.getUser().getId().equals(buyer.getUser().getId())){
            PostRequest savedPostRequest;
            if (request != null){
                post.setCategory(categoryRepository.findCategoryById(request.getCategoryId()));
                post.setSubCategory(subCategoryRepository.findSubCategoryById(request.getSubcategoryId()));
                post.setRecruitLevel(request.getRecruitLevel());
                post.setSkills(skillRepository.findAllByNameIn(request.getSkillsName()));
                post.setJobTitle(request.getJobTitle());
                post.setShortRequirement(request.getShortRequirement());
                post.setAttachFile(request.getAttachFile());
                post.setStatus(PostRequestStatus.OPEN);
                post.setContractCancelFee(request.getContractCancelFee());
                post.setBudget(request.getBudget());
                post.setUser(userRepository.findUserById(currentUser.getId()));
                savedPostRequest =  postRequestRepository.save(post);
                List<MilestoneContract> milestoneContractList = request.getMilestoneContracts();
                if (milestoneContractList != null){
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
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        List<PostRequest> postRequest ;
       // PostRequestResponse postRequestResponse;
        List<PostRequestResponse> postRequestResponses = new ArrayList<>();
        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)) {
            postRequest = postRequestRepository.findAllByUser_Id(currentUser.getId());
            for (PostRequest postRequest1 : postRequest) {

                postRequestResponses.add(new PostRequestResponse(postRequest1.getCategory().getId(), postRequest1.getSubCategory().getId(),
                        postRequest1.getRecruitLevel(), postRequest1.getSkills(), postRequest1.getJobTitle(), postRequest1.getShortRequirement(),
                        postRequest1.getAttachFile(), postRequest1.getMilestoneContracts(), postRequest1.getContractCancelFee(), postRequest1.getBudget()));
            }
        }
        return postRequestResponses;
    }

    @Override
    public List<PostRequestResponse> getPostRequestByCategoryId(UUID categoryId) {
        List<PostRequestResponse> postRequestResponses = new ArrayList<>();
        List<PostRequest> postRequests = postRequestRepository.findAllByCategory_Id(categoryId);
        for (PostRequest postRequest:postRequests){
            postRequestResponses.add(new PostRequestResponse(postRequest.getId(),postRequest.getJobTitle(),postRequest.getBudget(),
                    postRequest.getUser().getBuyer().getId(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
                    postRequest.getUser().getCity(),postRequest.getCreateAt()));
        }
        return postRequestResponses;
    }

    @Override
    public PostRequestResponse getPostRequestDetails(UUID postRequestId) {
        PostRequest postRequest = postRequestRepository.findPostRequestById(postRequestId);
        return new PostRequestResponse(postRequest.getCategory().getName(),postRequest.getSubCategory().getName(),postRequest.getRecruitLevel(),
                postRequest.getSkills(), postRequest.getJobTitle(), postRequest.getShortRequirement(), postRequest.getMilestoneContracts(),
                postRequest.getContractCancelFee(), postRequest.getBudget(),postRequest.getUser().getFirstName(),postRequest.getUser().getLastName(),
                postRequest.getUser().getCity(),postRequest.getUser().getCreateAt(),postRequestRepository.countPostRequestByUser_Id(postRequest.getUser().getId()));
//        return postRequestRepository.findPostRequestById(postRequestId);
    }

    @Override
    public ApiResponse sellerApplyRequest(UUID postRequestId, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found "));
        PostRequest post = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Post Request not found "));
        List<Seller> sellersApply ;
        if (seller.getUser().getId().equals(currentUser.getId()) &&
                seller.getUser().getIsEnabled().equals(Boolean.TRUE)){

                sellersApply = sellerRepository.findAllByPostRequests_Id(postRequestId);
                for (Seller sellerApply: sellersApply){
                    if (sellerApply.getId().equals(seller.getId())){
                        return new ApiResponse(Boolean.FALSE, "Bạn đã apply bài Post Request này");
                    }
                }
                sellersApply.add(seller);
                post.setSellersApplyRequest(sellersApply);
                postRequestRepository.save(post);

            return new ApiResponse(Boolean.TRUE,"Apply Post Request thành công");

        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
