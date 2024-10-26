package com.saferent.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageUploadResponse extends SfResponse{

    private String imageId;


    public ImageUploadResponse(String message, boolean success, String imageId) {
        super(message, success);
        this.imageId = imageId;
    }
}
