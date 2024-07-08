package mk.ukim.finki.web_seminarska.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mk.ukim.finki.web_seminarska.model.enumerations.SalonType;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Salon {

    public Salon(){
    }

    public Salon(String name, String address, byte[] image,
                 LocalTime open_time, LocalTime close_time,
                 String city, String phone_number, String email,
                 SalonType type, String description,
                 List<Uslugi> services) {
        this.name = name;
        this.address = address;
        this.image = image;
        this.open_time = open_time;
        this.close_time = close_time;
        this.city = city;
        this.phone_number = phone_number;
        this.email = email;
        this.type = type;
        this.description = description;
        this.services = services;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private byte[] image;

    private LocalTime open_time;

    private LocalTime close_time;

    private String city;

    private String phone_number;

    private String email;

    @Enumerated(EnumType.STRING)
    private SalonType type;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Uslugi> services;

}
