package org.javaMirea.taxi_app.service;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.entities.Car;
import org.javaMirea.taxi_app.entities.Role;
import org.javaMirea.taxi_app.entities.User;
import org.javaMirea.taxi_app.exceptions.CustomAuthException;
import org.javaMirea.taxi_app.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_.]+$");
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");


    /**
     * Сохранение пользователя, проверка полей
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        if (!isUserNull(user) &&
                usernamePattern.matcher(user.getUsername()).matches() &&
                emailPattern.matcher(user.getEmail()).matches())
            return userRepository.save(user);
        return user;
    }

    /**
     * Проверка необходимых полей пользователя на null
     *
     * @return true если необходимые поля пользователя не null
     */

    private boolean isUserNull(User user) {
        return user.getUsername() == null ||
                user.getEmail() == null ||
                user.getPassword() == null ||
                user.getRole() == null ||
                user.isDriver() && user.getCar() == null;
    }


    /**
     * Поиск пользователя в бд по id
     *
     * @return найденный пользователь
     */
    @Transactional(readOnly = true)
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }


    /**
     * Создание пользователя, проверка на уникальность
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomAuthException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomAuthException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени
     *
     * @return пользователь
     */
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        user.setCar(null);
        save(user);
    }

    /**
     * Выдача роли водителя текущему пользователю
     * Нужен для демонстрации
     */
    @Deprecated
    public void getDriver(Car car) {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_DRIVER);
        user.setCar(car);
        save(user);
    }

    /**
     * Выдача роли пользователя текущему пользователю
     * Нужен для демонстрации
     */
    @Deprecated
    public void getUser() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_USER);
        user.setCar(null);
        save(user);
    }

    /**
     * Получение списка всех пользователей из бд
     *
     * @return список пользователей
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
