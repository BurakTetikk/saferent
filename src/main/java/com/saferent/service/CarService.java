package com.saferent.service;

import com.saferent.dto.CarDTO;
import com.saferent.entity.Car;
import com.saferent.entity.ImageFile;
import com.saferent.exception.ConflictException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.mapper.CarMapper;
import com.saferent.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ImageFileService imageFileService;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, ImageFileService imageFileService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.imageFileService = imageFileService;
        this.carMapper = carMapper;
    }

    public void saveCar(String imageId, CarDTO carDTO) {

        //imageId repoda var mı?
        ImageFile imageFile = imageFileService.findImageById(imageId);



        // imageId daha önce başka bir araç için kullanıldı mı??
        Integer usedCarCount = carRepository.findCarCountByImageId(imageFile.getId());

        if (usedCarCount > 0) {

            throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);

        }

        // ** MAPPER **
        Car car = carMapper.carDTOToCar(carDTO);

        // ** IMAGE SET **
        Set<ImageFile> imageFiles = new HashSet<>();
        imageFiles.add(imageFile);

        car.setImageFiles(imageFiles);

        carRepository.save(car);


    }


    public List<CarDTO> getAllCars() {

        List<Car> carList = carRepository.findAll();

        return carMapper.map(carList);


    }
}
