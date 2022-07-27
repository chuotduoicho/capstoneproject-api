package com.jovinn.capstoneproject.thirdapi;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.jovinn.capstoneproject.dto.file.UploadFileResponse;
import com.jovinn.capstoneproject.enumerable.ImageType;
import com.jovinn.capstoneproject.exception.JovinnException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveManagerService implements FileManagerService {
    private final Drive googleDriveManager;
    @Value("${drive.avatar_folder}")
    private String AVATAR_FOLDER;
    @Value("${drive.box_folder}")
    private String BOX_FOLDER;
    @Value("${drive.delivery_folder}")
    private String DELIVERY_FOLDER;
    @Value("${drive.default_folder}")
    private String DEFAULT_FOLDER;

    @Override
    public UploadFileResponse uploadNewFile(MultipartFile file, ImageType imageType, UUID id) {
        try {
            validateUploadFile(file);
            File fileMetadata = new File();
            fileMetadata.setParents(Collections.singletonList(getFolderId(imageType)));
            fileMetadata.setName(generateFileName(file, imageType, id));
            File uploadFile = googleDriveManager.files()
                    .create(fileMetadata, new InputStreamContent(file.getContentType(),
                            new ByteArrayInputStream(file.getBytes())))
                    .setFields("id, webContentLink")
                    .execute();
            return UploadFileResponse.builder()
                    .url(uploadFile.getWebContentLink().replace("&export=download", ""))
                    .id(uploadFile.getId())
                    .build();
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        throw new JovinnException(HttpStatus.BAD_REQUEST, "Error");
    }

    @Override
    public About getInfo() {
        try {
            return googleDriveManager.about().get()
                    .setFields("user, storageQuota, maxUploadSize")
                    .execute();
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Error");
        }
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            googleDriveManager.files().delete(fileId).execute();
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Error");
        }
    }

    private void validateUploadFile(MultipartFile file){
        if (null == file) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Error");
        }
    }
    private String getFolderId(ImageType imageType) {
        switch (imageType) {
            case AVATAR:
                return AVATAR_FOLDER;
            case BOX:
                return BOX_FOLDER;
            case DELIVERY:
                return DELIVERY_FOLDER;
            default:
                return DEFAULT_FOLDER;
        }
    }
    private String generateFileName(MultipartFile file, ImageType imageType, UUID id) {
        return imageType.toString()
                + "_"
                + id.toString()
                + "_"
                + System.currentTimeMillis()
                + FilenameUtils.getExtension(file.getOriginalFilename());
    }
}
