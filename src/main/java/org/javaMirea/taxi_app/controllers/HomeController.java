package org.javaMirea.taxi_app.controllers;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.view.ViewBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final ViewBuilder viewBuilder;
    @GetMapping
    public String getHome(Model model) {
        viewBuilder.setMenu(model);
        return "home";
    }
}
