package com.saferent.repository;

import com.saferent.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    // ** JPQL **
    @Query("select count(*) from Car c join c.imageFiles imgFile where imgFile.id=:id")
    Integer findCarCountByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = {"imageFiles"})
    List<Car> findAll();


    @EntityGraph(attributePaths = {"imageFiles"})
    Page<Car> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"imageFiles"})
    Optional<Car> findCarById(Long id);


    @Query("select c from Car c join c.imageFiles im where im.id=:id")
    List<Car> findCarsByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = "id")
    List<Car> getAllBy();
}
