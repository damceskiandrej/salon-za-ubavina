package mk.ukim.finki.web_seminarska.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.model.enumerations.UserRole;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidSalonUserIdException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUsernameOrPasswordException;
import mk.ukim.finki.web_seminarska.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.web_seminarska.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.web_seminarska.repository.SalonUserRepository;
import mk.ukim.finki.web_seminarska.service.SalonUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SalonUserServiceImpl implements SalonUserService {
    private final SalonUserRepository salonUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SalonUser findById(Long id) {
       return salonUserRepository.findById(id).orElseThrow(InvalidSalonUserIdException::new);
    }

    @Override
    public List<SalonUser> listAll() {
        return salonUserRepository.findAll();
    }

    @Override
    public SalonUser findByUsername(String username) {
        return salonUserRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public SalonUser create(String name, String surname, String address, Integer phone_number, String city, String email, UserRole role, String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        SalonUser salonUser = new SalonUser(name, surname ,address, phone_number, city, email, role, username, encodedPassword);
        return salonUserRepository.save(salonUser);
    }

    @Override
    public SalonUser register(String username, String password, String repeatPassword, String name, String surname, UserRole role) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }

        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if(this.salonUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        SalonUser user = new SalonUser(username, passwordEncoder.encode(password), name, surname, role);

        return salonUserRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return salonUserRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }
}
