package mk.ukim.finki.web_seminarska.web;

import mk.ukim.finki.web_seminarska.model.enumerations.UserRole;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.web_seminarska.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.web_seminarska.model.exceptions.UsernameAlreadyExistsException;
import mk.ukim.finki.web_seminarska.service.AuthService;
import mk.ukim.finki.web_seminarska.service.SalonUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final SalonUserService userService;

    public RegisterController(SalonUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam UserRole role
    ) {
        try{
            this.userService.register(username, password, repeatedPassword, name, surname, role);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException | UsernameAlreadyExistsException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }
}
