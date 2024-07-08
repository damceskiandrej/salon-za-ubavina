package mk.ukim.finki.web_seminarska.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.service.UslugiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class UslugiController {
    private final UslugiService service;

    @GetMapping("/salonServices")
    public String listSalonServices(Model model){
        model.addAttribute("salonServices", this.service.ListAllSalonServices());
        return "uslugi";
    }

    @PostMapping("/salonServices")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Integer price,
                         @RequestParam Integer duration){
        this.service.create(name, description, price, duration);
        return "redirect:/salonServices";
    }

    @GetMapping("/salonServices/add")
    public String show_add(Model model){
        model.addAttribute("salonServices", this.service.ListAllSalonServices());
        return "form-usluga";
    }
}