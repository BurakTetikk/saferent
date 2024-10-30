package com.saferent.mapper;

import com.saferent.dto.request.ReservationRequest;
import com.saferent.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {


    Reservation reservationRequestToReservation(ReservationRequest reservationRequest);


}
