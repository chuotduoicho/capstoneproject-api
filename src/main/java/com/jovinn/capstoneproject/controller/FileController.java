package com.jovinn.capstoneproject.controller;

import com.google.api.services.drive.model.About;
import com.jovinn.capstoneproject.dto.client.file.UploadFileResponse;
import com.jovinn.capstoneproject.enumerable.ImageType;
import com.jovinn.capstoneproject.thirdapi.FileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/files")
@CrossOrigin(origins = "*")
@Validated
public class FileController {
    private final FileManagerService fileManagerService;
    @PutMapping("")
    public ResponseEntity<UploadFileResponse> uploadImage(@RequestPart(value = "file") MultipartFile multipartFile,
                                                          @RequestPart(value = "type") String imageType,
                                                          @RequestPart(value = "id") String id) {
        UploadFileResponse response = fileManagerService.uploadNewFile(multipartFile, ImageType.valueOf(imageType), UUID.fromString(id));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/about")
    public ResponseEntity<About> getInfo() {
        return ResponseEntity.ok(fileManagerService.getInfo());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable String fileId) {
        fileManagerService.deleteFile(fileId);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<String> downloadFile(@PathVariable("fileName") String fileName) {
        return ResponseEntity.ok(fileManagerService.downloadFile(fileName));
    }
}
