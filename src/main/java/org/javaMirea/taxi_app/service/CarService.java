package org.javaMirea.taxi_app.service;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.entities.Car;
import org.javaMirea.taxi_app.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final CarRepository carRepository;

    /**
     * Сохранение информации о машине
     *
     * @param car данные машины
     * @return сохранённая машина
     */
    public Car save(Car car) {
        return  carRepository.save(car);
    }

    /**
     * Поиск свободных машин
     *
     * @return список свободных машин
     */
    @Transactional(readOnly = true)
    public List<Car> findFreeCars() {
        return carRepository.findFreeCars();
    }

    /**
     * Поиск машины по id
     *
     * @param id id машины
     * @return найденная машина
     */
    @Transactional(readOnly = true)
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * Поиск всех машин
     *
     * @return список машин
     */
    @Transactional(readOnly = true)
    public List<Car> findAll() {
        return carRepository.findAll();
    }
}
