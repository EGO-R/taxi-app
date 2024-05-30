package org.javaMirea.taxi_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.javaMirea.taxi_app.entities.Drive;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class DriveDto {
    private Long id;
    private String passenger;
    private String driver;
    private String departureLocation;
    private String arrivalLocation;
    private String status;
    private String car;
    private String carId;

    public DriveDto(Drive drive) {
        this.id = drive.getId();
        this.passenger = drive.getPassenger().getUsername();
        if (drive.getDriver() != null) {
            this.driver = drive.getDriver().getUsername();
            this.car = drive.getCar().getMark();
            this.carId = drive.getCar().getId().toString();
        }
        else {
            this.driver = "В процессе поиска...";
            this.car = "Водитель не выбран...";
            this.carId = "-";
        }
        this.departureLocation = drive.getDepartureLocation();
        this.arrivalLocation = drive.getArrivalLocation();
        this.status = drive.getStatus().toString();
    }

    public static List<DriveDto> getDtoList(List<Drive> drives) {
        var driveDtos = new ArrayList<DriveDto>();
        for (var drive : drives)
            driveDtos.add(new DriveDto(drive));
        return driveDtos;
    }
}
