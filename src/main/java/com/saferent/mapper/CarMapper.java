package com.saferent.mapper;

import com.saferent.dto.CarDTO;
import com.saferent.entity.Car;
import com.saferent.entity.ImageFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "imageFiles", ignore = true)
    Car carDTOToCar(CarDTO carDTO);


    List<CarDTO> map(List<Car> car);

    @Mapping(source = "imageFiles", target = "imageFiles", qualifiedByName = "getImageAsString")
    CarDTO carToCarDTO(Car car);


    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {

        Set<String> images = new HashSet<>();

        images = imageFiles.stream().map(imageFile -> imageFile.getId().toString()).collect(Collectors.toSet());

        return images;


    }

}
