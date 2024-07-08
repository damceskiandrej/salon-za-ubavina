package mk.ukim.finki.web_seminarska.repository;

import mk.ukim.finki.web_seminarska.model.SalonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalonUserRepository extends JpaRepository<SalonUser, Long> {
    Optional<SalonUser> findByUsername(String username);

    Optional<SalonUser> findByUsernameAndPassword(String username, String password);
}
