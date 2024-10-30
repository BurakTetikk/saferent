package com.saferent.repository;

import com.saferent.entity.Reservation;
import com.saferent.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



    @Query("select r from Reservation r " +
            "join fetch Car c on r.car=c.id where " +
            "c.id=:carId and (r.status not in :statuses) and :pickUpTime between r.pickUpTime and r.dropOffTime " +
            "or " +
            "c.id=:carId and (r.status not in :statuses) and :dropOffTime between r.pickUpTime and r.dropOffTime " +
            "or " +
            "c.id=:carId and (r.status not in :statuses) and (r.pickUpTime between :pickUpTime and :dropOffTime)")
    List<Reservation> checkCarStatus(@Param("carId") Long carId,
                                     @Param("pickUpTime") LocalDateTime pickUpTime,
                                     @Param("dropOffTime") LocalDateTime dropOffTime,
                                     @Param("statuses") ReservationStatus[] statuses);



}
