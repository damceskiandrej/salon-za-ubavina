package mk.ukim.finki.web_seminarska.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Appointment {

    public Appointment(){
    }

    public Appointment(LocalDateTime start_time,
                       LocalDateTime end_time,
                       Salon salon, SalonUser user,
                       List<Uslugi> services) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.salon = salon;
        this.user = user;
        this.services = services;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

    @ManyToOne
    private Salon salon;

    @ManyToOne
    private SalonUser user;

    @ManyToMany
    private List<Uslugi> services;
}
