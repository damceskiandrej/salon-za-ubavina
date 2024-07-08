package mk.ukim.finki.web_seminarska.service;

import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.enumerations.SalonType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

public interface SalonService {

    Salon findById(Long id);

    List<Salon> ListAllSalons();

    Salon delete(Long id);

    Salon create(String name, String address, MultipartFile file,
                 LocalTime open_time, LocalTime close_time,
                 String city, String phone_number, String email,
                 SalonType type, String description,
                 List<Long> services);

    Salon update(Long Id, String name, String address, MultipartFile file,
                 LocalTime open_time, LocalTime close_time,
                 String city, String phone_number, String email,
                 SalonType type, String description,
                 List<Long> services);

    List<Salon> filter(String city, Long service);
}
