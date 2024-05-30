package org.javaMirea.taxi_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.javaMirea.taxi_app.entities.Car;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CarDto {
    private Long id;
    private String mark;

    public CarDto(Car car) {
        this.id = car.getId();
        this.mark = car.getMark();
    }
}
