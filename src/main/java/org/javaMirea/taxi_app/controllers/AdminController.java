package org.javaMirea.taxi_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.entities.Car;
import org.javaMirea.taxi_app.entities.Role;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.service.CarService;
import org.javaMirea.taxi_app.service.DriveService;
import org.javaMirea.taxi_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CarService carService;
    private final DriveService driveService;

    @GetMapping
    public String getMenu() {
        return "admin/home";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String getUserInfo(@PathVariable("id") Long userId,
                              Model model) {
        var optionalUser = userService.getById(userId);
        if (optionalUser.isEmpty())
            return "errors/not_found";
        var user = optionalUser.get();
        model.addAttribute("user", user);
        model.addAttribute("roles", Arrays.asList(Role.values()));
        model.addAttribute("cars", carService.findFreeCars());
        return "admin/user_info";
    }

    @PostMapping("/users/{id}")
    public void updateUser(@PathVariable("id") Long userId,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("role") Role role,
                           @RequestParam("carId") Long carId,
                           HttpServletResponse response) throws IOException {
        var optionalUser = userService.getById(userId);
        if (optionalUser.isEmpty())
            response.setStatus(404);

        var user = optionalUser.get();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        if (user.isDriver()) {
            var optionalCar = carService.findById(carId);
            if (optionalCar.isEmpty())
                response.sendRedirect("/admin/users/%s".formatted(userId));
            user.setCar(optionalCar.get());
        } else
            user.setCar(null);
        userService.save(user);

        response.sendRedirect("/admin/users/%s".formatted(userId));
    }

    @GetMapping("/drives")
    public String getDrives(Model model) {
        model.addAttribute("drives", driveService.findAll());
        return "admin/drives";
    }

    @GetMapping("/drives/{id}")
    public String getDriveInfo(@PathVariable("id") Long id,
                               Model model) {
        var optionalDrive = driveService.findById(id);
        if (optionalDrive.isEmpty())
            return "errors/not_found";
        var drive = optionalDrive.get();
        model.addAttribute("drive", drive);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("statuses", Arrays.asList(Status.values()));
        model.addAttribute("cars", carService.findAll());
        return "admin/drive_info";
    }

    @PostMapping("/drives/{id}")
    public void updateDriveInfo(@PathVariable("id") Long driveId,
                                @RequestParam("departureLocation") String departureLocation,
                                @RequestParam("arrivalLocation") String arrivalLocation,
                                @RequestParam("passenger") Long passengerId,
                                @RequestParam("status") Status status,
                                @RequestParam("driver") Long driverId,
                                @RequestParam("car") Long carId,
                                HttpServletResponse response) throws IOException {
        var optionalDrive = driveService.findById(driveId);
        if (optionalDrive.isEmpty())
            response.setStatus(404);
        var drive = optionalDrive.get();
        if (departureLocation.trim().isEmpty())
            response.sendRedirect("/admin/drives/%s".formatted(driveId));
        drive.setDepartureLocation(departureLocation);
        if (arrivalLocation.trim().isEmpty())
            response.sendRedirect("/admin/drives/%s".formatted(driveId));
        drive.setArrivalLocation(arrivalLocation);
        var optionalPassenger = userService.getById(passengerId);
        if (optionalPassenger.isEmpty())
            response.sendRedirect("/admin/drives/%s".formatted(driveId));
        drive.setPassenger(optionalPassenger.get());
        drive.setStatus(status);
        if (status == Status.AWAITING ||
                status == Status.CANCELLED) {
            drive.setDriver(null);
            drive.setCar(null);
        } else {
            var optionalDriver = userService.getById(driverId);
            if (optionalDriver.isEmpty())
                response.sendRedirect("/admin/drives/%s".formatted(driveId));
            drive.setDriver(optionalDriver.get());
            var optionalCar = carService.findById(carId);
            if (optionalCar.isEmpty())
                response.sendRedirect("/admin/drives/%s".formatted(driveId));
            drive.setCar(optionalCar.get());
        }
        driveService.save(drive);
        response.sendRedirect("/admin/drives/%s".formatted(driveId));
    }

    @GetMapping("/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", carService.findAll());
        return "admin/cars";
    }

    @GetMapping("/cars/{id}")
    public String getCarInfo(@PathVariable("id") Long carId,
                             Model model) {
        var optionalCar = carService.findById(carId);
        if (optionalCar.isEmpty())
            return "/errors/not_found";
        model.addAttribute("car", optionalCar.get());
        return "admin/car_info";
    }

    @PostMapping("/cars/{id}")
    public void updateCarInfo(@PathVariable("id") Long carId,
                                      @RequestParam("mark") String mark,
                                      HttpServletResponse response) throws IOException {
        var optionalCar = carService.findById(carId);
        if (optionalCar.isEmpty())
            response.setStatus(404);
        var car = optionalCar.get();
        if (mark.trim().isEmpty())
            response.sendRedirect("/admin/cars/%s".formatted(carId));
        car.setMark(mark);
        carService.save(car);
        response.sendRedirect("/admin/cars/%s".formatted(carId));
    }

    @GetMapping("/cars/add")
    public String carForm() {
        return "admin/add_car";
    }

    @PostMapping("/cars/add")
    public String addCar(@RequestParam("car_mark") String mark,
                         Model model) {
        var car = Car.builder()
                .mark(mark)
                .build();
        car = carService.save(car);

        if (car.getId() != null) {
            model.addAttribute("isAdded", true);
            model.addAttribute("car", car);
        }
        return "admin/add_car";
    }
}
