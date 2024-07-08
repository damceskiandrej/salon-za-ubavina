package mk.ukim.finki.web_seminarska.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.web_seminarska.repository.SalonUserRepository;
import mk.ukim.finki.web_seminarska.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final SalonUserRepository salonUserRepository;
    @Override
    public SalonUser login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }

        return salonUserRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }

}
