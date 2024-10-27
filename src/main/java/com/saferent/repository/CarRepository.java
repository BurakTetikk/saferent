package com.saferent.repository;

import com.saferent.entity.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    // ** JPQL **
    @Query("select count(*) from Car c join c.imageFiles imgFile where imgFile.id=:id")
    Integer findCarCountByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = {"imageFiles"})
    List<Car> findAll();
}
