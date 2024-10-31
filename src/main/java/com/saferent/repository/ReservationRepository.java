package com.saferent.repository;

import com.saferent.entity.Car;
import com.saferent.entity.Reservation;
import com.saferent.entity.User;
import com.saferent.entity.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    @EntityGraph(attributePaths = {"car, car.imageFiles"})
    List<Reservation> findAll();


    @EntityGraph(attributePaths = {"car, car.imageFiles"})
    Page<Reservation> findAll(Pageable pageable);


    @EntityGraph(attributePaths = {"car", "car.imageFiles", "user"})
    Optional<Reservation> findById(Long id);


    @EntityGraph(attributePaths = {"car", "car.imageFiles", "user"})
    Page<Reservation> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"car", "car.imageFiles", "user"})
    Optional<Reservation> findByIdAndUser(Long id, User user);

    boolean existsByCar(Car car);


}
