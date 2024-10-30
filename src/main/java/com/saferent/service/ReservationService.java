package com.saferent.service;

import com.saferent.dto.request.ReservationRequest;
import com.saferent.entity.Car;
import com.saferent.entity.Reservation;
import com.saferent.entity.User;
import com.saferent.entity.enums.ReservationStatus;
import com.saferent.exception.BadRequestException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.mapper.ReservationMapper;
import com.saferent.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public void createReservation(ReservationRequest reservationRequest, User user, Car car) {

        checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        boolean carStatus = checkCarAvailability(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);

        if (carStatus) {

            reservation.setStatus(ReservationStatus.CREATED);

        } else {

            throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);

        }

        reservation.setCar(car);
        reservation.setUser(user);
        Double totalPrice = getTotalPrice(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);

    }


    // İstenen rezervasyon tarihleri doğru mu? Örn: başlangıç 10 Aralık bitiş 5 Aralık --- alınan ve verilen tarih aynı olmamalı
    private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        LocalDateTime now = LocalDateTime.now();

        if (pickUpTime.isBefore(now)) {

            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);

        }

        boolean isEqual = pickUpTime.isEqual(dropOffTime)?true:false;

        boolean isBefore = pickUpTime.isBefore(dropOffTime)?true:false;


        if (isEqual || !isBefore) {

            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);

        }

    }


    // Rezervasyonlar arası çakışma var mı??
    private List<Reservation> getConflictReservation(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        if (pickUpTime.isAfter(dropOffTime)) {

            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);

        }

        ReservationStatus[] statuses = {ReservationStatus.CANCELLED, ReservationStatus.DONE};


        List<Reservation> existReservation = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, statuses);

        return existReservation;

    }

    // Araç müsait mi?
    private boolean checkCarAvailability(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        List<Reservation> existReservations = getConflictReservation(car, pickUpTime, dropOffTime);

        return existReservations.isEmpty();

    }

    // Fiyat hesaplama
    private Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOffTime);

        double hours = Math.ceil(minutes / 60.0);

        return car.getPricePerHour() * hours;

    }




}
