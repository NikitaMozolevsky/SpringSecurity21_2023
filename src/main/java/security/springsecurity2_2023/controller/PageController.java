package security.springsecurity2_2023.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/registration")
    public String toRegistrationPage() {
        return "registration";
    }

    @GetMapping("/unregistered")
    public String toUnregisteredUserPage() {
        return "unregistered.html";
    }

    @GetMapping("/registered")
    public String toRegisteredUserPage() {
        return "registered.html";
    }

}
