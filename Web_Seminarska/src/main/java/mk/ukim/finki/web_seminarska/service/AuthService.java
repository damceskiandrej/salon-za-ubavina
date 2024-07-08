package mk.ukim.finki.web_seminarska.service;

import mk.ukim.finki.web_seminarska.model.SalonUser;

import java.util.List;

public interface AuthService {

    SalonUser login(String username, String password);

    List<SalonUser> findAll();
}
