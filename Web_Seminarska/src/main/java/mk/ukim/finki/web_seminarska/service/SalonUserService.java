package mk.ukim.finki.web_seminarska.service;

import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.model.enumerations.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface SalonUserService extends UserDetailsService {

    List<SalonUser> listAll();

    SalonUser findByUsername(String username);

    SalonUser create(String name, String surname,
                     String address, Integer phone_number,
                     String city, String email,
                     UserRole role, String username, String password);

    SalonUser register(String username, String password,
                       String repeatPassword, String name,
                       String surname, UserRole role);
}
