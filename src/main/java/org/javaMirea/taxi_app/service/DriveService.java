package org.javaMirea.taxi_app.service;

import lombok.RequiredArgsConstructor;
import org.javaMirea.taxi_app.dtos.DriveDto;
import org.javaMirea.taxi_app.entities.Drive;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.entities.User;
import org.javaMirea.taxi_app.repositories.DriveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class DriveService {
    private final DriveRepository driveRepository;

    /**
     * Поиск поездки по пользователю
     *
     * @param user данные пользователя
     * @return список поездок пользователя
     */
    @Transactional(readOnly = true)
    public List<DriveDto> findByUser(User user) {
        var byPassenger = driveRepository.findByPassenger(user);
        var byDriver = driveRepository.findByDriver(user);
        var drives = Stream.concat(byPassenger.stream(), byDriver.stream()).toList();
        return DriveDto.getDtoList(drives);
    }

    /**
     * Поиск поездки по id
     *
     * @param id id поездки
     * @return найденная поездка
     */
    @Transactional(readOnly = true)
    public Optional<Drive> findById(Long id) {
        return driveRepository.findById(id);
    }

    /**
     * Проверка принадлежности пользователя к поездке
     *
     * @param drive поездка
     * @param user пользователь
     * @return true если пользователь принадлежит к поездке или админ
     */
    public boolean isBelongsToDrive(Drive drive, User user) {
        var isBelongs = drive.getPassenger().getId().equals(user.getId()) ||
                user.isAdmin();

        if (drive.getDriver() != null)
            isBelongs = isBelongs || drive.getDriver().getId().equals(user.getId());

        return isBelongs;
    }

    /**
     * Сохранение поездки в бд
     *
     * @param drive поездка
     * @return сохранённая поездка
     */
    public Drive save(Drive drive) {
        return driveRepository.save(drive);
    }

    /**
     * Поиск поездок по статусу
     *
     * @param status статус поездки
     * @return список найденных поездок
     */
    @Transactional(readOnly = true)
    public List<DriveDto> findByStatus(Status status) {
        var drives = driveRepository.findByStatus(status);
        return DriveDto.getDtoList(drives);
    }

    /**
     * Поиск всех поездок
     *
     * @return список всех поездок
     */
    @Transactional(readOnly = true)
    public List<Drive> findAll() {
        return driveRepository.findAll();
    }
}
