package com.saferent.service;

import com.saferent.dto.CarDTO;
import com.saferent.entity.Car;
import com.saferent.entity.ImageFile;
import com.saferent.exception.BadRequestException;
import com.saferent.exception.ConflictException;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.mapper.CarMapper;
import com.saferent.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final ImageFileService imageFileService;

    private final ReservationService reservationService;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, ImageFileService imageFileService, ReservationService reservationService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.imageFileService = imageFileService;
        this.reservationService = reservationService;
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

    public Page<CarDTO> getAllCarsWithPage(Pageable pageable) {

        Page<Car> carPage = carRepository.findAll(pageable);

        return carPage.map(car -> carMapper.carToCarDTO(car));


    }

    public CarDTO findById(Long id) {

        // ** ID CHECK **
        Car car = getCar(id);

        return carMapper.carToCarDTO(car);

    }

    public void updateCar(Long id, String imageId, CarDTO carDTO) {

        Car car = getCar(id);

        // ** CHECK BUILTIN **
        if (car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        ImageFile imageFile = imageFileService.findImageById(imageId);
        List<Car> carList = carRepository.findCarsByImageId(imageFile.getId());

        for (Car c : carList) {
            if (car.getId().longValue() != c.getId().longValue()) {
                throw new ConflictException(ErrorMessage.IMAGE_USED_MESSAGE);
            }
        }

        car.setAge(carDTO.getAge());
        car.setAirConditioning(carDTO.getAirConditioning());
        car.setBuiltIn(carDTO.getBuiltIn());
        car.setDoors(carDTO.getDoors());
        car.setFuelType(carDTO.getFuelType());
        car.setLuggage(carDTO.getLuggage());
        car.setModel(carDTO.getModel());
        car.setPricePerHour(carDTO.getPricePerHour());
        car.setSeats(carDTO.getSeats());
        car.setTransmission(carDTO.getTransmission());

        car.getImageFiles().add(imageFile);

        carRepository.save(car);


    }

    public void removeById(Long id) {

        Car car = getCar(id);

        if (car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        boolean exist = reservationService.existByCar(car);

        if (exist) {
            throw new BadRequestException(ErrorMessage.CAR_USED_BY_RESERVATION_MESSAGE);
        }

        carRepository.delete(car);

    }


    private Car getCar(Long id) {

        Car car = carRepository
                .findCarById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return car;

    }

    public Car getCarById(Long carId) {

        Car car = carRepository
                .findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, carId)));

        return car;

    }

    public List<Car> getCars() {

        return carRepository.getAllBy();

    }

    // imageId: f289f1e1-4b32-48f8-9ac5-604ea5f060cf



}
