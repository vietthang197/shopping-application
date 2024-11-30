package vn.com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.shop.entity.Role;
import vn.com.shop.services.RoleService;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listRoles(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Page<Role> rolePage = roleService.getAllRoles(PageRequest.of(page, size));
        model.addAttribute("roles", rolePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rolePage.getTotalPages());
        model.addAttribute("totalItems", rolePage.getTotalElements());
        return "role/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "role/form";
    }

    @PostMapping("/create")
    public String createRole(@ModelAttribute Role role) {
        roleService.createRole(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "role/form";
    }

    @PostMapping("/edit/{id}")
    public String updateRole(@PathVariable String id, @ModelAttribute Role role) {
        roleService.updateRole(id, role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return "redirect:/admin/roles";
    }
}