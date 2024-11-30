package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.com.shop.dto.OrderStatistics;
import vn.com.shop.services.OrderService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showDashboard(@RequestParam(required = false, defaultValue = "month") String period,
                                @RequestParam(required = false) Integer year,
                                Model model) {
        if (year == null) {
            year = LocalDateTime.now().getYear();
        }

        OrderStatistics stats = orderService.getStatistics(year, period);
        model.addAttribute("stats", stats);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedPeriod", period);
        model.addAttribute("currentYear", LocalDateTime.now().getYear());

        return "admin";
    }
}
