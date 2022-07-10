package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.CommentRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.CommentResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface CommentService {
    CommentResponse add(UUID contractId, CommentRequest request, UserPrincipal currentUser);
    CommentResponse update(UUID id, CommentRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
