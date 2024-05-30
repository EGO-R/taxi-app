package org.javaMirea.taxi_app.repositories;

import org.javaMirea.taxi_app.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(value = """
            SELECT c.id,
                   c.mark
            FROM users u
            RIGHT JOIN cars c on u.car_id = c.id
            WHERE u.car_id is null;""", nativeQuery = true)
    List<Car> findFreeCars();
}
