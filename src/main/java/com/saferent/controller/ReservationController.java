package com.saferent.controller;

import com.saferent.dto.CarDTO;
import com.saferent.dto.ReservationDTO;
import com.saferent.dto.request.ReservationRequest;
import com.saferent.dto.request.ReservationUpdateRequest;
import com.saferent.dto.response.CarAvailabilityResponse;
import com.saferent.dto.response.ResponseMessage;
import com.saferent.dto.response.SfResponse;
import com.saferent.entity.Car;
import com.saferent.entity.User;
import com.saferent.service.CarService;
import com.saferent.service.ReservationService;
import com.saferent.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {

        List<ReservationDTO> reservationDTOList = reservationService.getAllReservations();

        return ResponseEntity.ok(reservationDTOList);

    }

    @GetMapping("/admin/all/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllReservationsWithPage(@RequestParam("page") int page,
                                                                           @RequestParam("size") int size,
                                                                           @RequestParam("sort") String prop,
                                                                           @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {


        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<ReservationDTO> reservationDTOList = reservationService.getAllWithPage(pageable);

        return ResponseEntity.ok(reservationDTOList);

    }

    // ** CHECK CAR AVAİLABLE **
    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<SfResponse> checkCarIsAvailable(@RequestParam("carId") Long carId,
                                                          @RequestParam("pickUpDateTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpTime,
                                                          @RequestParam("dropOffDateTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffTime) {


        Car car = carService.getCarById(carId);

        boolean isAvailable = reservationService.checkCarAvailability(car, pickUpTime, dropOffTime);

        Double totalPrice = reservationService.getTotalPrice(car, pickUpTime, dropOffTime);

        SfResponse response = new CarAvailabilityResponse(ResponseMessage.CAR_AVAILABLE_RESPONSE_MESSAGE, true, isAvailable, totalPrice);

        return ResponseEntity.ok(response);

    }

    // ** UPDATE RESERVATION **
    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> updateReservation(@RequestParam("carId") Long carId,
                                                        @RequestParam("reservationId") Long reservationId,
                                                        @Valid @RequestBody ReservationUpdateRequest request) {



        Car car = carService.getCarById(carId);
        reservationService.updateReservation(reservationId, car, request);

        SfResponse response = new SfResponse(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {

        ReservationDTO reservationDTO = reservationService.getReservationDTO(id);

        return ResponseEntity.ok(reservationDTO);


    }

    @GetMapping("/admin/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllUserReservations(@RequestParam("userId") Long userId,
                                                                       @RequestParam("page") int page,
                                                                       @RequestParam("size") int size,
                                                                       @RequestParam("sort") String prop,
                                                                       @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getById(userId);

        Page<ReservationDTO> reservationDTOS = reservationService.findReservationPageByUser(user, pageable);

        return ResponseEntity.ok(reservationDTOS);


    }


    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<ReservationDTO> getUserReservationById(@PathVariable("id") Long id) {

        User user = userService.getCurrentUser();

        ReservationDTO reservationDTO = reservationService.findByIdAndUser(id, user);

        return ResponseEntity.ok(reservationDTO);

    }

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<Page<ReservationDTO>> getAllReservation(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("sort") String prop,
                                                                  @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getCurrentUser();

        Page<ReservationDTO> reservationDTOS = reservationService.findReservationPageByUser(user, pageable);

        return ResponseEntity.ok(reservationDTOS);

    }


    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteReservation(@PathVariable("id") Long id) {

        reservationService.removeById(id);

        SfResponse response = new SfResponse(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(response);

    }





}
