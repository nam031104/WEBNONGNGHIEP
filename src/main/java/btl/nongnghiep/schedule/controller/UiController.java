package btl.nongnghiep.schedule.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UiController {

    @GetMapping("/control")
    public String getControlPage(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "control";
    }

    @GetMapping("/schedule")
    public String getSchedulePage(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "control"; // Assuming your schedule template is here
    }
}
