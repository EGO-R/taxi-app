package org.javaMirea.taxi_app.view;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class ViewBuilder {
    private final UserService userService;

    /**
     * Установка необходимых условий для отображения меню
     *
     * @param model шаблон
     */
    public void setMenu(Model model) {
        model.addAttribute("isDriver", userService.getCurrentUser().isDriver());
        model.addAttribute("isAdmin", userService.getCurrentUser().isAdmin());
    }
}
