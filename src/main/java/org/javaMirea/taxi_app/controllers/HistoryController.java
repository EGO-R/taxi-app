package org.javaMirea.taxi_app.controllers;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.service.DriveService;
import org.javaMirea.taxi_app.service.UserService;
import org.javaMirea.taxi_app.view.ViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final DriveService driveService;
    private final UserService userService;
    private final ViewBuilder viewBuilder;
    @GetMapping
    public String getHistory(Model model) {
        viewBuilder.setMenu(model);
        var user = userService.getCurrentUser();
        var driveDtos = driveService.findByUser(user);
        model.addAttribute("drives", driveDtos);
        return "history";
    }
}
