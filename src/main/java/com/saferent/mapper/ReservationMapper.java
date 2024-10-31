package com.saferent.mapper;

import com.saferent.dto.ReservationDTO;
import com.saferent.dto.request.ReservationRequest;
import com.saferent.entity.ImageFile;
import com.saferent.entity.Reservation;
import com.saferent.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReservationMapper {


    Reservation reservationRequestToReservation(ReservationRequest reservationRequest);

    @Mapping(source = "car.imageFiles", target = "carDTO.imageFiles", qualifiedByName = "getImageAsString")
    @Mapping(source = "user", target = "userId", qualifiedByName = "getUserId")
    ReservationDTO reservationToReservationDTO(Reservation reservation);


    List<ReservationDTO> map(List<Reservation> reservationList);

    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {

        Set<String> images = new HashSet<>();

        images = imageFiles
                .stream()
                .map(imageFile -> imageFile.getId().toString())
                .collect(Collectors.toSet());

        return images;
    }

    @Named("getUserId")
    public static Long getUserId(User user) {

        return user.getId();

    }




}
