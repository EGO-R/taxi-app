package org.javaMirea.taxi_app.controllers;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.service.DriveService;
import org.javaMirea.taxi_app.view.ViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/awaiting")
@RequiredArgsConstructor
public class AwaitingController {
    private final DriveService driveService;
    private final ViewBuilder viewBuilder;


    @GetMapping
    public String getAvailableDrives(Model model) {
        viewBuilder.setMenu(model);
        model.addAttribute("drives", driveService.findByStatus(Status.AWAITING));
        return "awaiting";
    }

}
