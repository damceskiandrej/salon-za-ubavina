package mk.ukim.finki.web_seminarska.repository;

import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.Uslugi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {
    List<Salon> findAllByCityAndServicesContaining(String city, Uslugi service);
    List<Salon> findAllByCity(String city);
    List<Salon> findAllByServicesContaining(Uslugi service);

}
