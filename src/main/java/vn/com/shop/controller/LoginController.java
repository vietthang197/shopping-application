package vn.com.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.shop.dto.RegisterRequest;
import vn.com.shop.services.AccountService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {

    private final AccountService accountService;

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra password matching
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword",
                    "Passwords do not match");
        }

        if (result.hasErrors()) {
            String error = result.getAllErrors().getFirst().getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/register";
        }

        try {
            accountService.registerNewAccount(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful!");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("username", "error.username", e.getMessage());
            return "register";
        }
    }
}
