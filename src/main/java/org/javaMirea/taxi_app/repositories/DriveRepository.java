package org.javaMirea.taxi_app.repositories;

import org.javaMirea.taxi_app.entities.Drive;
import org.javaMirea.taxi_app.entities.Status;
import org.javaMirea.taxi_app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriveRepository extends JpaRepository<Drive, Long> {
    List<Drive> findByStatus(Status status);
    List<Drive> findByPassenger(User user);
    List<Drive> findByDriver(User user);

}
