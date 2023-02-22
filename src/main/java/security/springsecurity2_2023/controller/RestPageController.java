package security.springsecurity2_2023.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RestPageController {

    private final PageController pageController;

    @GetMapping("/registration")
    public String something() {
        return "registration";
    }

}
