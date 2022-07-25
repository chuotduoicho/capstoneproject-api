package com.jovinn.capstoneproject.thirdapi;

import com.google.api.services.drive.model.About;
import com.jovinn.capstoneproject.dto.file.UploadFileResponse;
import com.jovinn.capstoneproject.enumerable.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileManagerService {
    UploadFileResponse uploadNewFile(MultipartFile file, ImageType imageType, UUID id);
    About getInfo();
    void deleteFile(String fileId);
}
