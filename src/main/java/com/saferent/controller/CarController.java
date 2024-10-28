package com.saferent.controller;

import com.saferent.dto.CarDTO;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;


    public CarController(CarService carService) {
        this.carService = carService;
    }



    // ** SAVE CAR **
    @PostMapping("/admin/{imageId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> saveCar(@PathVariable("imageId") String imageId,
                                              @Valid @RequestBody CarDTO carDTO) {

        carService.saveCar(imageId, carDTO);

        SfResponse response = new SfResponse(ResponseMessage.CAR_SAVED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }


    @GetMapping("/visitors/all")
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> allCars = carService.getAllCars();

        return ResponseEntity.ok(allCars);


    }

    @GetMapping("/visitors/pages")
    public ResponseEntity<Page<CarDTO>> getAllCarsWithPage(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sort") String prop,
                                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC")Sort.Direction direction) {


        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<CarDTO> carDTOS = carService.getAllCarsWithPage(pageable);

        return ResponseEntity.ok(carDTOS);

    }



    @GetMapping("/visitors/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable("id") Long id) {

        CarDTO carDTO = carService.findById(id);

        return ResponseEntity.ok(carDTO);

    }

    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> updateCar(
            @RequestParam("id") Long id,
            @RequestParam("imageId") String imageId,
            @Valid @RequestBody CarDTO carDTO) {

        carService.updateCar(id, imageId, carDTO);

        SfResponse response = new SfResponse(ResponseMessage.CAR_UPDATED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteCar(@PathVariable("id") Long id) {

        carService.removeById(id);

        SfResponse response = new SfResponse(ResponseMessage.CAR_DELETED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }




}
