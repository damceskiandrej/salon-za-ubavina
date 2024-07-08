package mk.ukim.finki.web_seminarska.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.Appointment;
import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.Uslugi;
import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidAppointmentIdException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidSalonIdException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidSalonUserIdException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUslugiIdException;
import mk.ukim.finki.web_seminarska.repository.AppointmentRepository;
import mk.ukim.finki.web_seminarska.repository.SalonRepository;
import mk.ukim.finki.web_seminarska.repository.UslugiRepository;
import mk.ukim.finki.web_seminarska.repository.SalonUserRepository;
import mk.ukim.finki.web_seminarska.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SalonRepository salonRepository;
    private final UslugiRepository salonServicesRepository;
    private final SalonUserRepository salonUserRepository;



    @Override
    public List<Appointment> listAppointmentsByUser(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Appointment create(LocalDateTime start_time, LocalDateTime end_time, Long salonId, Long userId, List<Long> services) {
        Salon salon = salonRepository.findById(salonId).orElseThrow(InvalidSalonIdException::new);
        SalonUser user = salonUserRepository.findById(userId).orElseThrow(InvalidSalonUserIdException::new);
        List<Uslugi> serviceList = salonServicesRepository.findAllById(services);
        Appointment appointment = new Appointment(start_time, end_time, salon, user, serviceList);if (checkAppointment(appointment)) {
            appointmentRepository.save(appointment);
            return appointment;
        } else {
            throw new RuntimeException("Обидете се повторно за 30 секунди");
        }
    }



    @Override
    public Appointment delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(InvalidAppointmentIdException::new);
        appointmentRepository.delete(appointment);
        return appointment;
    }

    @Override
    public boolean checkAppointment(Appointment newAppointment) {
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                newAppointment.getStart_time(), newAppointment.getEnd_time());
        return overlappingAppointments.isEmpty();
    }

    @Override
    public List<LocalDateTime> getAvailableTimeSlots(Long salonId, LocalDateTime startDate, int days) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0); // Почеток на работното време
        LocalTime end = LocalTime.of(17, 0); // Крај на работното време

        for (int i = 0; i < days; i++) {
            LocalDateTime dateTime = startDate.plusDays(i).with(start);
            if (dateTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
                while (dateTime.toLocalTime().isBefore(end)) {
                    if (isSlotAvailable(salonId, dateTime)) {
                        availableSlots.add(dateTime);
                    }
                    dateTime = dateTime.plusMinutes(30);
                }
            }
        }

        return availableSlots;
    }

    private boolean isSlotAvailable(Long salonId, LocalDateTime start) {
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                start, start.plusMinutes(30));
        return overlappingAppointments.isEmpty();
    }
}