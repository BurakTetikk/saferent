package com.saferent.service;

import com.saferent.dto.request.ReservationRequest;
import com.saferent.entity.Car;
import com.saferent.entity.User;
import com.saferent.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void createReservation(ReservationRequest reservationRequest, User user, Car car) {




    }
}
