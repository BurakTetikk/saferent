package com.saferent.controller;

import com.saferent.dto.response.ImageUploadResponse;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.entity.ImageFile;
import com.saferent.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class ImageFileController {

    private final ImageFileService imageFileService;


    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }


    // ** UPLOAD **
    // imageId : 83066f23-5a7c-40c9-bcd3-7d065a1ee29d
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageUploadResponse> uploadFile(@RequestParam("file")MultipartFile file) {

        String imageId = imageFileService.saveImage(file);

        ImageUploadResponse response = new ImageUploadResponse(ResponseMessage.IMAGE_UPLOADED_RESPONSE_MESSAGE, true, imageId);

        return ResponseEntity.ok(response);

    }


    // ** DOWNLOAD **
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") String id) {

        ImageFile imageFile = imageFileService.downloadImage(id);


        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + imageFile.getName())
                .body(imageFile.getImageData().getData());


    }


}
