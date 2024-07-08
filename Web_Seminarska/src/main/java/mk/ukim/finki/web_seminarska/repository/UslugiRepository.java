package mk.ukim.finki.web_seminarska.repository;

import mk.ukim.finki.web_seminarska.model.Uslugi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UslugiRepository extends JpaRepository<Uslugi, Long> {

}
