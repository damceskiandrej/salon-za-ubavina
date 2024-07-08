package mk.ukim.finki.web_seminarska.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.Appointment;
import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.repository.SalonUserRepository;
import mk.ukim.finki.web_seminarska.service.AppointmentService;
import mk.ukim.finki.web_seminarska.service.SalonService;
import mk.ukim.finki.web_seminarska.service.SalonUserService;
import mk.ukim.finki.web_seminarska.service.UslugiService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final SalonService salonService;
    private final SalonUserService salonUserService;
    private final UslugiService uslugiService;
    @GetMapping("/appointments")
    public String listAppointments(Authentication authentication, Model model) {

        SalonUser user = salonUserService.findByUsername(authentication.getName());
        List<Appointment> appointments = appointmentService.listAppointmentsByUser(user.getId());
        model.addAttribute("appointments", appointments);
        return "appointment-list";
    }

    @GetMapping("/appointments/{salonId}/add")
    public String showAdd(Model model, @PathVariable Long salonId) {
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<LocalDateTime> availableTimeSlots = appointmentService.getAvailableTimeSlots(salonId, startDate, 5);

        Salon salon = salonService.findById(salonId);
        model.addAttribute("availableTimeSlots", availableTimeSlots);
        model.addAttribute("salons", salonService.ListAllSalons());
        model.addAttribute("services", uslugiService.ListAllSalonServices());
        model.addAttribute("salon", salon);

        return "appointment-form";
    }

    @PostMapping("/appointments/{salonId}/create")
    public String create(@RequestParam LocalDateTime start_time,
                         @PathVariable Long salonId,
                         @RequestParam List<Long> services,
                         Authentication authentication,
                         Model model) {
        SalonUser user = salonUserService.findByUsername(authentication.getName());
        Long userId = user.getId();

        LocalDateTime endTime = start_time.plusHours(1); // Assuming appointments are 1 hour long

        try {
            appointmentService.create(start_time, endTime, salonId, userId, services);
            return "redirect:/appointments";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "appointment-form";
        }
    }

    @PostMapping("/appointments/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.appointmentService.delete(id);
        return "redirect:/appointments";
    }
}