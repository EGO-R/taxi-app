package org.javaMirea.taxi_app.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.dtos.JwtAuthenticationResponse;
import org.javaMirea.taxi_app.dtos.UserDto;
import org.javaMirea.taxi_app.entities.Role;
import org.javaMirea.taxi_app.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param userDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(UserDto userDto) {

        var user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getRawPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param userDto данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUsername(),
                userDto.getRawPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(userDto.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Добавление cookie в response
     * Срок жизни cookie - 1 час
     *
     * @param response ответ сервера
     * @param jwtResp токен
     */
    public void addCookie(HttpServletResponse response,
                          JwtAuthenticationResponse jwtResp) {
        var authCookie = new Cookie("token", jwtResp.getToken());
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        authCookie.setSecure(false);
        authCookie.setMaxAge(60 * 60);
        response.addCookie(authCookie);
    }
}
