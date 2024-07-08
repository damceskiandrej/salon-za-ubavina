package mk.ukim.finki.web_seminarska.repository;

import jakarta.persistence.LockModeType;
import mk.ukim.finki.web_seminarska.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.salon.id = :salonId AND (a.start_time < :end_time AND a.end_time > :start_time)")
    List<Appointment> findOverlappingAppointments(@Param("salonId") Long salonId, @Param("start_time") LocalDateTime start_time, @Param("end_time") LocalDateTime end_time);


    List<Appointment> findByUserId(Long userId);
}