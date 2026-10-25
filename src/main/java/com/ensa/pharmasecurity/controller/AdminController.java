package com.ensa.pharmasecurity.controller;

import com.ensa.pharmasecurity.model.User;
import com.ensa.pharmasecurity.repository.RoleRepository;
import com.ensa.pharmasecurity.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public AdminController(
            UserRepository userRepo,
            RoleRepository roleRepo,
            PasswordEncoder encoder) {

        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("roles", roleRepo.findAll());
        return "admin";
    }

    @PostMapping("/users")
    public String creer(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role) {

        if (username.isBlank() || password.isBlank()) {
            return "redirect:/admin";
        }

        if (userRepo.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.getRoles().add(
                roleRepo.findByName(role).orElseThrow());

            userRepo.save(user);
        }

        return "redirect:/admin";
    }

    @PostMapping("/users/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        userRepo.findById(id).ifPresent(user -> {
            if (!"admin".equals(user.getUsername())) {
                userRepo.delete(user);
            }
        });

        return "redirect:/admin";
    }
}