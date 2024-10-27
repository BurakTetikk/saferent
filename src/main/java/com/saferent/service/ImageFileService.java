package com.saferent.service;

import com.saferent.dto.ImageFileDTO;
import com.saferent.entity.ImageData;
import com.saferent.entity.ImageFile;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;


    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    public String saveImage(MultipartFile file) {

        ImageFile imageFile = null;


        // ** NAME **
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getName()));

        // ** DATA **
        try {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile = new ImageFile(imageName, file.getContentType(), imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        imageFileRepository.save(imageFile);

        return imageFile.getId();


    }

    public ImageFile getImagebyId(String id) {

        ImageFile imageFile = imageFileRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));

        return imageFile;


    }

    public List<ImageFileDTO> getAllImages() {

        List<ImageFile> imageFiles = imageFileRepository.findAll();

        List<ImageFileDTO> fileDTOS = imageFiles.stream()
                .map(imageFile -> {
                    String imageUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/download/")
                            .path(imageFile.getId()).toUriString();

                    return new ImageFileDTO(imageFile.getName(), imageFile.getType(), imageFile.getLength(), imageUri);


                }).collect(Collectors.toList());


        return fileDTOS;


    }

    public void removeById(String id) {

        ImageFile imageFiles = getImagebyId(id);

        imageFileRepository.delete(imageFiles);

    }
}
