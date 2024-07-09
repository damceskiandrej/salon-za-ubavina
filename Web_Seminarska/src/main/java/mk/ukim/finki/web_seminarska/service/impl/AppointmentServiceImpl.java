package mk.ukim.finki.web_seminarska.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.*;
import mk.ukim.finki.web_seminarska.model.exceptions.*;
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
    public synchronized Appointment create(LocalDateTime start_time, LocalDateTime end_time, Long salonId, Long userId, List<Long> services) {
        Salon salon = salonRepository.findById(salonId).orElseThrow(InvalidSalonIdException::new);
        SalonUser user = salonUserRepository.findById(userId).orElseThrow(InvalidSalonUserIdException::new);
        List<Uslugi> serviceList = salonServicesRepository.findAllById(services);
        Appointment appointment = new Appointment(start_time, end_time, salon, user, serviceList);
        if (!checkAppointment(appointment)) {
            throw new RuntimeException("Терминот веќе е закажан. Обидете се повторно.");

        } else {
            appointmentRepository.save(appointment);
            return appointment;
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
                newAppointment.getSalon().getId(), newAppointment.getStart_time(), newAppointment.getEnd_time());
        return overlappingAppointments.isEmpty();
    }

    @Override
    public List<TimeSlot> getAvailableTimeSlots(Long salonId, LocalDateTime startDate, int days) {
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        for (int i = 0; i < days; i++) {
            LocalDateTime dateTime = startDate.plusDays(i).with(start);
            if (dateTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
                while (dateTime.toLocalTime().isBefore(end)) {
                    boolean isAvailable = isSlotAvailable(salonId, dateTime);
                    availableSlots.add(new TimeSlot(dateTime, isAvailable));
                    dateTime = dateTime.plusMinutes(60);
                }
            }
        }
        return availableSlots;
    }

    private boolean isSlotAvailable(Long salonId, LocalDateTime start) {
        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(
                salonId, start, start.plusMinutes(60));
        return overlappingAppointments.isEmpty();
    }
}