package mk.ukim.finki.web_seminarska.service;

import mk.ukim.finki.web_seminarska.model.Uslugi;

import java.util.List;

public interface UslugiService{

    List<Uslugi> ListAllSalonServices();

    Uslugi create(String name, String description,
                  Integer price, Integer duration);

}
