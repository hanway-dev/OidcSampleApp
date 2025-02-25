package org.example.oidcsampleapp.Controller;
import org.example.oidcsampleapp.Services.HUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private final HUserService hUserService;

    public UserController(HUserService hUserService) {
        this.hUserService = hUserService;
    }

    @GetMapping("/whoami")
    public String whoami(Model model) {
        var user = this.hUserService.getCurrentUser();
        model.addAttribute("user", user);

        return "whoami";
    }
}
