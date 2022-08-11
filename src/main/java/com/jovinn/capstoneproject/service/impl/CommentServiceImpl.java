package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.CommentRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.CommentResponse;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Comment;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.repository.CommentRepository;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentResponse add(UUID contractId, CommentRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            Comment comment = new Comment(contract.getSeller().getUser().getId(), UserActivityType.SELLER,
                    contract.getSeller().getBrandName(),
                    request.getText(), contract);
            Comment update = commentRepository.save(comment);
            return getResponse(update);
        } else if (contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            Comment comment = new Comment(contract.getBuyer().getUser().getId(), UserActivityType.BUYER,
                    contract.getBuyer().getUser().getLastName() + " " + contract.getBuyer().getUser().getFirstName(),
                    request.getText(), contract);
            Comment update = commentRepository.save(comment);
            return getResponse(update);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public CommentResponse update(UUID id, CommentRequest request, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy comment"));
        if (comment.getUserId().equals(currentUser.getId())) {
            comment.setText(request.getText());
            Comment update = commentRepository.save(comment);
            return getResponse(update);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy comment"));
        if (comment.getUserId().equals(currentUser.getId())) {
            commentRepository.deleteById(comment.getId());
            return new ApiResponse(Boolean.TRUE, "Xoá thành công comment");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<CommentResponse> getCommentsByContract(UUID contractId) {
        List<Comment> comments = commentRepository.findAllByContractIdOrderByCreateAtAsc(contractId);
        return comments.stream().map(
                        comment -> modelMapper.map(comment, CommentResponse.class))
                        .collect(Collectors.toList());
    }

    private CommentResponse getResponse(Comment update) {
        return new CommentResponse(update.getId(), update.getUserId(),
                update.getContract().getId(), update.getType(), update.getName(), update.getText());
    }
}
