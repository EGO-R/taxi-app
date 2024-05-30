package org.javaMirea.taxi_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.dtos.DriveDto;
import org.javaMirea.taxi_app.entities.Role;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.service.DriveService;
import org.javaMirea.taxi_app.service.UserService;
import org.javaMirea.taxi_app.view.ViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/drive")
@RequiredArgsConstructor
public class DriveInfoController {
    private final UserService userService;
    private final DriveService driveService;
    private final ViewBuilder viewBuilder;

    @GetMapping("/{id}")
    public String getDriveInfo(@PathVariable("id") Long driveId,
                               Model model,
                               HttpServletResponse response) {
        viewBuilder.setMenu(model);
        var optionalDrive = driveService.findById(driveId);
        if (optionalDrive.isEmpty()) {
            response.setStatus(404);
            return "errors/not_found";
        }
        var user = userService.getCurrentUser();
        var drive = optionalDrive.get();
        if (!(driveService.isBelongsToDrive(drive, user) ||
                drive.getStatus() == Status.AWAITING && user.isDriver())) {
            response.setStatus(403);
            return "errors/not_allowed";
        }

        var driveDto = new DriveDto(drive);
        model.addAttribute("drive", driveDto);
        model.addAttribute("ifOngoing", drive.getStatus() == Status.ONGOING);
        model.addAttribute("ifAwaiting", drive.getStatus() == Status.AWAITING);
        model.addAttribute("ifDriver", user.getRole() == Role.ROLE_DRIVER);

        return "drive_info";
    }

    @GetMapping("/finish/{id}")
    public void finishDrive(@PathVariable("id") Long id,
                            HttpServletResponse response) throws IOException {
        var user = userService.getCurrentUser();
        var optDrive = driveService.findById(id);
        if (optDrive.isPresent()) {
            var drive = optDrive.get();
            if (driveService.isBelongsToDrive(drive, user) &&
                    drive.getStatus() == Status.ONGOING) {
                drive.setStatus(Status.FINISHED);
                driveService.save(drive);
            }
        }
        response.sendRedirect("/drive/%s".formatted(id));
    }

    @GetMapping("/accept/{id}")
    public void acceptDrive(@PathVariable("id") Long id,
                            HttpServletResponse response) throws IOException {
        var optionalDrive = driveService.findById(id);
        var user = userService.getCurrentUser();
        if (optionalDrive.isPresent()) {
            var drive = optionalDrive.get();
            if (drive.getStatus() == Status.AWAITING && user.isDriver()) {
                drive.setDriver(user);
                drive.setCar(user.getCar());
                drive.setStatus(Status.ONGOING);
                driveService.save(drive);
            }
        }
        response.sendRedirect("/drive/%s".formatted(id));
    }

    @GetMapping("/cancel/{id}")
    public void cancelDrive(@PathVariable("id") Long driveId,
                            HttpServletResponse response) throws IOException {
        var optionalDrive = driveService.findById(driveId);
        var user = userService.getCurrentUser();
        if (optionalDrive.isPresent()) {
            var drive = optionalDrive.get();
            if (drive.getStatus() == Status.AWAITING && driveService.isBelongsToDrive(drive, user)) {
                drive.setStatus(Status.CANCELLED);
                driveService.save(drive);
            }
        }
        response.sendRedirect("/drive/%s".formatted(driveId));
    }

}
