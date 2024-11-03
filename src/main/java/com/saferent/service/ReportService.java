package com.saferent.service;

import com.saferent.entity.Car;
import com.saferent.entity.Reservation;
import com.saferent.entity.User;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.report.ExcelReporter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    private final CarService carService;

    private final UserService userService;

    private final ReservationService reservationService;


    public ReportService(CarService carService, UserService userService, ReservationService reservationService) {
        this.carService = carService;
        this.userService = userService;
        this.reservationService = reservationService;
    }


    public ByteArrayInputStream getUserReport() {

        List<User> users = userService.getUsers();

        try {
            return ExcelReporter.getUserExcelReport(users);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
        }

    }

    public ByteArrayInputStream getCarReport() {

        List<Car> cars = carService.getCars();

        try {
            return ExcelReporter.getCarExcelReport(cars);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
        }

    }

    public ByteArrayInputStream getReservationReport() {

        List<Reservation> reservations = reservationService.getAll();

        try {
            return ExcelReporter.getReservationExcelReport(reservations);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
        }

    }
}
