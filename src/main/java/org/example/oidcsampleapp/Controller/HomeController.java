package org.example.oidcsampleapp.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/secure")
    public String secure(@AuthenticationPrincipal OidcUser user, Model model) {
        model.addAttribute("userName", user.getFullName());
        return "secure";
    }

    @GetMapping("/logout_callback")
    public String logoutCallbacked() {
        return "redirect:/";
    }
}
