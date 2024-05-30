package org.javaMirea.taxi_app.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "drives")
public class Drive {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drive_id_seq")
    @SequenceGenerator(name = "drive_id_seq", sequenceName = "drive_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @Column(name = "departure_location", nullable = false)
    private String departureLocation;

    @Column(name = "arrival_location", nullable = false)
    private String arrivalLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
