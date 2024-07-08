package mk.ukim.finki.web_seminarska.service;

import mk.ukim.finki.web_seminarska.model.*;
import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentService {

    Appointment findById(Long id);

    List<Appointment> ListAllAppointments();

    List<Appointment> listAppointmentsByUser(Long userId);

    Appointment create(LocalDateTime start_time,
                       LocalDateTime end_time,
                       Long salons,
                       Long users,
                       List<Long> services);

    Appointment update(Long Id, LocalDateTime start_time,
                       LocalDateTime end_time,
                       Long salons,
                       Long users,
                       List<Long> services);

    Appointment delete(Long id);
    boolean checkAppointment(Appointment newAppointment);

    List<LocalDateTime> getAvailableTimeSlots(Long salonId, LocalDateTime date, int i);

}
