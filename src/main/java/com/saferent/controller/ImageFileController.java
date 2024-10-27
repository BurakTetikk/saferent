package com.saferent.controller;

import com.saferent.dto.ImageFileDTO;
import com.saferent.dto.response.ImageUploadResponse;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.entity.ImageFile;
import com.saferent.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

        ImageFile imageFile = imageFileService.getImagebyId(id);


        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + imageFile.getName())
                .body(imageFile.getImageData().getData());


    }


    // ** DISPLAY **
    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayFile(@PathVariable("id") String id) {

        ImageFile imageFile = imageFileService.getImagebyId(id);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);


        return new ResponseEntity<>(imageFile.getImageData().getData(), headers, HttpStatus.OK);




    }


    // ** GET ALL IMAGES **
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages() {

        List<ImageFileDTO> dtoList = imageFileService.getAllImages();

        return ResponseEntity.ok(dtoList);


    }


    // ** DELETE **
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteImageFile(@PathVariable("id") String id) {
        imageFileService.removeById(id);

        SfResponse response = new SfResponse(ResponseMessage.IMAGE_DELETED_RESPONSE_MESSAGE, true);


        return ResponseEntity.ok(response);

    }



}
