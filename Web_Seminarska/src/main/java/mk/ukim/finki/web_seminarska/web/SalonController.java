package mk.ukim.finki.web_seminarska.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.Salon;
import mk.ukim.finki.web_seminarska.model.enumerations.SalonType;
import mk.ukim.finki.web_seminarska.service.SalonService;
import mk.ukim.finki.web_seminarska.service.UslugiService;
import mk.ukim.finki.web_seminarska.service.SalonUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class SalonController {

    private final SalonService salonService;
    private final UslugiService salonServicesService;
    private final SalonUserService salonUserService;

    @GetMapping({"/"})
    public String listSalons(@RequestParam(required = false) String city,
                             @RequestParam(required = false) Long service,
                             Model model,
                             HttpSession session) {
        List<Salon> salons;
        if (city == null && service == null) {
            salons = this.salonService.ListAllSalons();
        } else {
            salons = this.salonService.filter(city, service);
            session.setAttribute("cityFilter", city);
            session.setAttribute("serviceFilter", service);
        }
        HashMap<Salon, String> images = new HashMap<>();
        salons.forEach(item -> {
            images.put(item, Base64.getEncoder().encodeToString(item.getImage()));
        });
        model.addAttribute("salons", salons);
        model.addAttribute("images", images);
        model.addAttribute("salonServices", salonServicesService.ListAllSalonServices());
        model.addAttribute("salonUsers", salonUserService.listAll());
        model.addAttribute("city", city);
        model.addAttribute("service", service);
        return "base-template";
    }

    @GetMapping("/salons/add")
    public String showAdd(Model model)
    {
        model.addAttribute("types", SalonType.values());
        model.addAttribute("salonServices", salonServicesService.ListAllSalonServices());
        return "form";
    }

    @GetMapping("/salons/{id}/edit")
    public String showEdit(@PathVariable Long id,
                           Model model)
    {
        Salon salon = salonService.findById(id);
        model.addAttribute("image", Base64.getEncoder().encodeToString(salon.getImage()));
        model.addAttribute("types", SalonType.values());
        model.addAttribute("salon", salonService.findById(id));
        model.addAttribute("salonServices", salonServicesService.ListAllSalonServices());
        model.addAttribute("salonUsers", salonUserService.listAll());
        return "form";
    }

    @PostMapping("/salons")
    public String create(@RequestParam String name,
                         @RequestParam String address,
                         @RequestParam MultipartFile file,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime open_time,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime close_time,
                         @RequestParam String city,
                         @RequestParam String phone_number,
                         @RequestParam String email,
                         @RequestParam SalonType type,
                         @RequestParam String description,
                         @RequestParam List<Long> services){
        this.salonService.create(name, address, file, open_time, close_time, city, phone_number, email, type, description, services);
        return "redirect:/";
    }

    @PostMapping("/salons/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String name,
                       @RequestParam String address,
                       @RequestParam MultipartFile file,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime open_time,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime close_time,
                       @RequestParam String city,
                       @RequestParam String phone_number,
                       @RequestParam String email,
                       @RequestParam SalonType type,
                       @RequestParam String description,
                       @RequestParam List<Long> services){
        this.salonService.update(id, name, address, file, open_time, close_time, city, phone_number, email, type, description, services);
        return "redirect:/";
    }

    @PostMapping("/salons/{id}/delete")
    public String delete(@PathVariable Long id){
       this.salonService.delete(id);
       return "redirect:/";
    }
}
