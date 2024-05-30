package org.javaMirea.taxi_app.controllers;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.dtos.UserDto;
import org.javaMirea.taxi_app.exceptions.CustomAuthException;
import org.javaMirea.taxi_app.service.AuthenticationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @GetMapping
    public String getForm(@RequestParam(value = "error", required = false) String error,
                          Model model) {
        if (error != null)
            model.addAttribute("isError", true);
        return "auth";
    }

    // TODO: 30.05.2024 проверить нужность
    @GetMapping("/error")
    public String getFormError(Model model) {
        model.addAttribute("isError", true);
        return "auth";
    }


    @PostMapping("/sign-up")
    public void signUp(@RequestParam("username") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam("password") String password,
                                            HttpServletResponse response) throws IOException {
        var userDto = UserDto.builder()
                .username(username)
                .email(email)
                .rawPassword(password)
                .build();

        var jwtResp = authenticationService.signUp(userDto);
        authenticationService.addCookie(response, jwtResp);
        response.sendRedirect("/home");
    }



    @PostMapping("/sign-in")
    public void signIn(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpServletResponse response) throws IOException {
        var userDto = UserDto.builder()
                .username(username)
                .rawPassword(password)
                .build();

        var jwtResp = authenticationService.signIn(userDto);
        authenticationService.addCookie(response, jwtResp);
        response.sendRedirect("/home");
    }

    @ExceptionHandler({CustomAuthException.class, UsernameNotFoundException.class})
    public void signUpExceptionHandler(HttpServletResponse response) throws IOException {
        response.sendRedirect("/auth?error=1");
        response.setStatus(400);
    }
}
