package com.saferent.dto;

import com.saferent.entity.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {


    private Long id;

    @Size(max = 30, message = "Size is exceeded!!")
    @NotBlank(message = "Please provide car model!!")
    private String model;

    @NotNull(message = "Please provide car doors info!!")
    private Integer doors;

    @NotNull(message = "Please provide car seats info!!")
    private Integer seats;

    @NotNull(message = "Please provide car luggage info!!")
    private Integer luggage;

    @NotBlank(message = "Please provide car transmission info!!")
    private String transmission;

    @NotNull(message = "Please provide car airCondition info!!")
    private Boolean airConditioning;

    @NotNull(message = "Please provide car age info!!")
    private Integer age;

    @NotNull(message = "Please provide car pricePerHour info!!")
    private Double pricePerHour;

    @Size(max = 30, message = "Size is exceeded!!")
    @NotBlank(message = "Please provide car fuelType!!")
    private String fuelType;

    private Boolean builtIn = false;


    private Set<String> imageFiles;



}
