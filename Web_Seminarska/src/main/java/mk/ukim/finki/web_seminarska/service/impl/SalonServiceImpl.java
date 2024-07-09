package mk.ukim.finki.web_seminarska.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.Uslugi;
import mk.ukim.finki.web_seminarska.model.enumerations.SalonType;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidSalonIdException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUslugiIdException;
import mk.ukim.finki.web_seminarska.repository.SalonRepository;
import mk.ukim.finki.web_seminarska.repository.UslugiRepository;
import mk.ukim.finki.web_seminarska.service.SalonService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
@Service
@AllArgsConstructor
public class SalonServiceImpl implements SalonService {
    private final SalonRepository salonRepository;
    private final UslugiRepository salonServicesRepository;
    @Override
    public Salon findById(Long id) {
        return salonRepository.findById(id).orElseThrow(InvalidSalonIdException::new);
    }

    @Override
    public List<Salon> ListAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon delete(Long id) {
        Salon salon = salonRepository.findById(id).orElseThrow(InvalidSalonIdException::new);
        salonRepository.delete(salon);
        return salon;
    }

    @Override
    public Salon create(String name, String address, MultipartFile file, LocalTime open_time, LocalTime close_time, String city, String phone_number, String email, SalonType type, String description, List<Long> services) {
        List<Uslugi> salonServices = salonServicesRepository.findAllById(services);
        Salon salon = null;
        try {
            byte[] imageBytes = IOUtils.toByteArray(new URL("https://salonlfc.com/wp-content/uploads/2018/01/image-not-found-1-scaled-1150x647.png"));
            salon = new Salon(name, address, !file.isEmpty() ? file.getBytes() : imageBytes, open_time,close_time,city,phone_number,email,type,description,salonServices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        salonRepository.save(salon);
        return salon;
    }

    @Override
    public Salon update(Long Id, String name, String address, MultipartFile file,LocalTime open_time, LocalTime close_time, String city, String phone_number, String email, SalonType type, String description, List<Long> services) {
        List<Uslugi> salonServices = salonServicesRepository.findAllById(services);
        Salon salon = salonRepository.findById(Id).orElseThrow(InvalidSalonIdException::new);
        salon.setName(name);
        salon.setAddress(address);
        if (!file.isEmpty()) {
            try {
                salon.setImage(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        salon.setOpen_time(open_time);
        salon.setClose_time(close_time);
        salon.setCity(city);
        salon.setPhone_number(phone_number);
        salon.setEmail(email);
        salon.setType(type);
        salon.setDescription(description);
        salon.setServices(salonServices);
        salonRepository.save(salon);
        return salon;
    }

//    public List<Salon> filter(String city, Long service) {
//        if (city != null && !city.isEmpty() && service != null) {
//            Uslugi salonServices = salonServicesRepository.findById(service)
//                    .orElseThrow(InvalidUslugiIdException::new);
//            return salonRepository.findAllByCityAndServicesContaining(city, salonServices);
//        }
//        else if (city != null && !city.isEmpty() && service == null) {
//            return salonRepository.findAllByCity(city);
//        }
//        else if (service != null && (city == null || city.isEmpty())) {
//            Uslugi salonServices = salonServicesRepository.findById(service)
//                    .orElseThrow(InvalidUslugiIdException::new);
//            return salonRepository.findAllByServicesContaining(salonServices);
//        }
//        else {
//            return salonRepository.findAll();
//        }
//    }

    @Override
    public List<Salon> filter(String city, Long service, HttpSession session) {
        // Save the filter values in session
        session.setAttribute("selectedCity", city);
        session.setAttribute("selectedService", service);

        if (city != null && !city.isEmpty() && service != null) {
            Uslugi salonServices = salonServicesRepository.findById(service)
                    .orElseThrow(InvalidUslugiIdException::new);
            return salonRepository.findAllByCityAndServicesContaining(city, salonServices);
        } else if (city != null && !city.isEmpty() && service == null) {
            return salonRepository.findAllByCity(city);
        } else if (service != null && (city == null || city.isEmpty())) {
            Uslugi salonServices = salonServicesRepository.findById(service)
                    .orElseThrow(InvalidUslugiIdException::new);
            return salonRepository.findAllByServicesContaining(salonServices);
        } else {
            return salonRepository.findAll();
        }
    }


}
