package mk.ukim.finki.web_seminarska.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.Uslugi;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUslugiIdException;
import mk.ukim.finki.web_seminarska.repository.UslugiRepository;
import mk.ukim.finki.web_seminarska.service.UslugiService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UslugiServiceImpl implements UslugiService {
    private final UslugiRepository uslugiRepository;
    @Override
    public Uslugi findById(Long id) {
        return uslugiRepository.findById(id).orElseThrow(InvalidUslugiIdException::new);
    }

    @Override
    public List<Uslugi> ListAllSalonServices() {
        return uslugiRepository.findAll();
    }

    @Override
    public Uslugi create(String name, String description, Integer price, Integer duration) {
        Uslugi salonServices = new Uslugi(name, description, price, duration);
        uslugiRepository.save(salonServices);
        return salonServices;
    }


}