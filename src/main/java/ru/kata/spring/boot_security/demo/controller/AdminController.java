package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listAllUsers(Model model, @AuthenticationPrincipal UserDetails userDetailsl) {
        User currentUser = userService.findUserByName(userDetailsl.getUsername());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin";
    }


    @GetMapping("/new")
    public String showNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllUsers());
        return "admin";
    }


    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleIds) {
        Set<Role> roles = roleService.findRoleByIds(roleIds);
        user.setRoles(roles);
        userService.createUser(user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                roles);
        return "redirect:/admin";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleIds) {
        Set<Role> roles = roleService.findRoleByIds(roleIds);
        user.setRoles(roles);
        userService.updateUser(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRoles());
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUserById(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/find")
    public String findUserById(@RequestParam Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("foundUser", user);
        return "admin";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAllRoles());
        return "edit";
    }
}
