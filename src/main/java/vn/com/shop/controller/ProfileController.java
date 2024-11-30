package vn.com.shop.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.shop.dto.PasswordChangeRequest;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.dto.ProfileUpdateRequest;
import vn.com.shop.entity.Account;
import vn.com.shop.services.AccountService;
import vn.com.shop.services.ProductCategoryService;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public String viewProfile(Model model) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);

        model.addAttribute("account", account);
        model.addAttribute("profileUpdateRequest", new ProfileUpdateRequest());
        model.addAttribute("passwordChangeRequest", new PasswordChangeRequest());

        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid ProfileUpdateRequest request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            return "profile/index";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            accountService.updateProfile(username, request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid PasswordChangeRequest request,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile/index";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            accountService.changePassword(username, request);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profile";
    }
}
