package com.jovinn.capstoneproject.dto.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileResponse {
    String url;
    String id;
}
