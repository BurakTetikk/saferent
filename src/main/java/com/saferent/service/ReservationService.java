package com.saferent.service;

import com.saferent.dto.ReservationDTO;
import com.saferent.dto.request.ReservationRequest;
import com.saferent.dto.request.ReservationUpdateRequest;
import com.saferent.entity.Car;
import com.saferent.entity.Reservation;
import com.saferent.entity.User;
import com.saferent.entity.enums.ReservationStatus;
import com.saferent.exception.BadRequestException;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.mapper.ReservationMapper;
import com.saferent.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

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
    public List<Reservation> getConflictReservation(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        if (pickUpTime.isAfter(dropOffTime)) {

            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);

        }

        ReservationStatus[] statuses = {ReservationStatus.CANCELLED, ReservationStatus.DONE};


        List<Reservation> existReservation = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, statuses);

        return existReservation;

    }

    // Araç müsait mi?
    public boolean checkCarAvailability(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        List<Reservation> existReservations = getConflictReservation(car, pickUpTime, dropOffTime);

        return existReservations.isEmpty();

    }

    // Fiyat hesaplama
    public Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {

        Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOffTime);

        double hours = Math.ceil(minutes / 60.0);

        return car.getPricePerHour() * hours;

    }


    public List<ReservationDTO> getAllReservations() {

        List<Reservation> reservations = reservationRepository.findAll();

        return reservationMapper.map(reservations);

    }

    public Page<ReservationDTO> getAllWithPage(Pageable pageable) {

        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

        return reservationPage.map(reservationMapper::reservationToReservationDTO);

    }

    public void updateReservation(Long reservationId, Car car, ReservationUpdateRequest request) {

        Reservation reservation = getById(reservationId);

        if (reservation.getStatus().equals(ReservationStatus.CANCELLED) || reservation.getStatus().equals(ReservationStatus.DONE)) {

            throw new BadRequestException(ErrorMessage.RESERVATION_STATUS_CANT_CHANGE_MESSAGE);

        }

        if (request.getStatus() != null && request.getStatus() == ReservationStatus.CREATED) {

            checkReservationTimeIsCorrect(request.getPickUpTime(), request.getDropOffTime());

            List<Reservation> conflictReservatiions = getConflictReservation(car, request.getPickUpTime(), request.getDropOffTime());


            if (!conflictReservatiions.isEmpty()) {

                if (!(conflictReservatiions.size() == 1 && conflictReservatiions.get(0).equals(reservationId))){
                    throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
                }

            }


            Double totalPrice = getTotalPrice(car, request.getPickUpTime(), request.getDropOffTime());

            reservation.setTotalPrice(totalPrice);
            reservation.setCar(car);


        }

        reservation.setPickUpTime(request.getPickUpTime());
        reservation.setDropOffTime(request.getDropOffTime());
        reservation.setDropOffLocation(request.getDropOffLocation());
        reservation.setPickUpLocation(request.getPickUpLocation());
        reservation.setStatus(request.getStatus());

        reservationRepository.save(reservation);

    }

    public Reservation getById(Long id) {

        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return reservation;

    }

    public ReservationDTO getReservationDTO(Long id) {

        Reservation reservation = getById(id);

        return reservationMapper.reservationToReservationDTO(reservation);

    }

    public Page<ReservationDTO> findReservationPageByUser(User user, Pageable pageable) {

        Page<Reservation> reservationPage = reservationRepository.findAllByUser(user, pageable);

        return reservationPage.map(reservationMapper::reservationToReservationDTO);

    }

    public ReservationDTO findByIdAndUser(Long id, User user) {

        Reservation reservation = reservationRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        return reservationMapper.reservationToReservationDTO(reservation);

    }

    public void removeById(Long id) {

        /*Reservation reservation = getById(id);

        reservationRepository.delete(reservation);*/


        boolean exist = reservationRepository.existsById(id);

        if (!exist) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id));
        }

        reservationRepository.deleteById(id);

    }

    public boolean existByCar(Car car) {

        return reservationRepository.existsByCar(car);

    }
}
