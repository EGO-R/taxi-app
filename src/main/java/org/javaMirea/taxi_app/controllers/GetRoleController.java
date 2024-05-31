package org.javaMirea.taxi_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.service.CarService;
import org.javaMirea.taxi_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/get")
public class GetRoleController {
    private final UserService userService;
    private final CarService carService;

    @GetMapping("/admin")
    public void getAdmin(HttpServletResponse response) throws IOException {
        userService.getAdmin();
        response.sendRedirect("/admin");
    }

    @GetMapping("/driver")
    public String selectCar(Model model) {
        model.addAttribute("cars", carService.findFreeCars());
        return "roles/get_driver";
    }

    @PostMapping("/driver")
    public String getDriver(@RequestParam("carId") Long carId) {
        var optionalCar = carService.findById(carId);
        if (optionalCar.isEmpty())
            return "errors/not_found";
        var car = optionalCar.get();
        userService.getDriver(car);
        return "roles/greet_driver";
    }



    @GetMapping("/user")
    public String getUser() {
        userService.getUser();
        return "roles/greet_user";
    }
}
