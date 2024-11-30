package vn.com.shop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.com.shop.dto.AccountDto;
import vn.com.shop.dto.ProductCategoryDto;
import vn.com.shop.dto.ShoppingCart;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Customer;
import vn.com.shop.entity.Orders;
import vn.com.shop.mapper.AccountMapper;
import vn.com.shop.request.CheckoutRequest;
import vn.com.shop.services.AccountService;
import vn.com.shop.services.CustomerService;
import vn.com.shop.services.OrderService;
import vn.com.shop.services.ProductCategoryService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public String showCheckoutForm(Model model, HttpSession session) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        CheckoutRequest checkoutRequest = new CheckoutRequest();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        List<Customer> customerList= customerService.findLatestByAccount(account);
        if (!CollectionUtils.isEmpty(customerList)) {
            Customer customer = customerList.getFirst();
            checkoutRequest.setFullName(customer.getFullName());
            checkoutRequest.setPhone(customer.getPhone());
            checkoutRequest.setAddress(customer.getAddress());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("checkoutRequest", checkoutRequest);
        return "checkout/index";
    }

    @PostMapping
    public String processCheckout(@Valid CheckoutRequest request,
                                  BindingResult result,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes, Model model) {
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.findAll();
        model.addAttribute("categories", productCategoryDtos);
        if (result.hasErrors()) {
            return "checkout/index";
        }

        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Orders order = orderService.createOrder(request, cart, username);

            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");

            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
}
