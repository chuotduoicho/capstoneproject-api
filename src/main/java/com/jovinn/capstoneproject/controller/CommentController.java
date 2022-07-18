package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.CommentRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.CommentResponse;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comment")
@CrossOrigin(origins = "*")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/{contractId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable("contractId") UUID contractId,
                                                      @Valid @RequestBody CommentRequest request,
                                                      @CurrentUser UserPrincipal currentUser) {
        CommentResponse response = commentService.add(contractId, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("id") UUID id,
                                                      @Valid @RequestBody CommentRequest request,
                                                      @CurrentUser UserPrincipal currentUser) {
        CommentResponse response = commentService.update(id, request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("id") UUID id,
                                                     @CurrentUser UserPrincipal currentUser) {
        ApiResponse response = commentService.delete(id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
