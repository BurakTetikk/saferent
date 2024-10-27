package com.saferent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Integer luggage; // bagaj

    @Column(length = 30, nullable = false)
    private String transmission; // vites

    @Column(nullable = false)
    private Boolean airConditioning; // klima

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double pricePerHour;

    @Column(length = 30, nullable = false)
    private String fuelType;

    private Boolean builtIn = false;

    @OneToMany(orphanRemoval = true) //
    @JoinColumn(name = "car_id")
    private Set<ImageFile> imageFiles;

}
