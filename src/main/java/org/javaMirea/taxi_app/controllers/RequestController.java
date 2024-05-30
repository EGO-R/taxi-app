package org.javaMirea.taxi_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.entities.Drive;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.service.DriveService;
import org.javaMirea.taxi_app.service.UserService;
import org.javaMirea.taxi_app.view.ViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    private final UserService userService;
    private final DriveService driveService;
    private final ViewBuilder viewBuilder;

    @GetMapping
    public String getForm(Model model) {
        viewBuilder.setMenu(model);
        return "request";
    }

    @PostMapping
    public void addRequest(@RequestParam("department") String department,
                           @RequestParam("target") String target,
                           HttpServletResponse response) throws IOException {
        var drive = Drive.builder()
                .passenger(userService.getCurrentUser())
                .departureLocation(department)
                .arrivalLocation(target)
                .status(Status.AWAITING)
                .build();
        drive = driveService.save(drive);
        response.sendRedirect("/drive/%s".formatted(drive.getId()));
    }

}
