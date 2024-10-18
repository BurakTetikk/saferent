package com.saferent.controller;

import com.saferent.service.ImageFileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class ImageFileController {

    private final ImageFileService imageFileService;


    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }
}
