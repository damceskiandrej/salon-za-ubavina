package mk.ukim.finki.web_seminarska.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mk.ukim.finki.web_seminarska.model.SalonUser;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.web_seminarska.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.web_seminarska.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    @GetMapping
    public String getLoginPage() {

        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        SalonUser user = null;
        try {
            user = authService.login(request.getParameter("username"), request.getParameter("password"));
        } catch (InvalidUserCredentialsException | InvalidArgumentsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "login";
        }
        request.getSession().setAttribute("user", user);
        return "redirect:/";
    }
}