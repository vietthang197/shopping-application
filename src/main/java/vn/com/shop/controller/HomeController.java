package vn.com.shop.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Log4j2
public class HomeController {

    @GetMapping
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "home";
    }
}
