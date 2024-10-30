package com.saferent.controller;

import com.saferent.dto.CarDTO;
import com.saferent.dto.request.ReservationRequest;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.entity.Car;
import com.saferent.entity.User;
import com.saferent.service.CarService;
import com.saferent.service.ReservationService;
import com.saferent.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    private final CarService carService;

    private final UserService userService;


    public ReservationController(ReservationService reservationService, CarService carService, UserService userService) {
        this.reservationService = reservationService;
        this.carService = carService;
        this.userService = userService;
    }



    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> makeReservation(@RequestParam("carId") Long carId, @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarById(carId);

        User user = userService.getCurrentUser();

        reservationService.createReservation(reservationRequest, user, car);

        SfResponse response = new SfResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }


    // ADMIN make reservation
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> addReservation(@RequestParam("userId") Long userId,
                                                     @RequestParam("carId") Long carId,
                                                     @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarById(carId);

        User user = userService.getById(userId);

        reservationService.createReservation(reservationRequest, user, car);

        SfResponse response = new SfResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }




}
