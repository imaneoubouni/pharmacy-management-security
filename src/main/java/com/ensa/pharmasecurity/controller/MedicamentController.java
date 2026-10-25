package com.ensa.pharmasecurity.controller;

import com.ensa.pharmasecurity.model.Medicament;
import com.ensa.pharmasecurity.repository.MedicamentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicaments")
public class MedicamentController {

    private final MedicamentRepository medRepo;

    public MedicamentController(MedicamentRepository medRepo) {
        this.medRepo = medRepo;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("medicaments", medRepo.findAll());
        model.addAttribute("nouveau", new Medicament());
        return "medicaments";
    }

    @PostMapping("/ajouter")
    public String ajouter(@ModelAttribute Medicament medicament) {
        medicament.setId(null);
        medRepo.save(medicament);
        return "redirect:/medicaments";
    }

    @PostMapping("/stock/{id}")
    public String modifierStock(
            @PathVariable Long id,
            @RequestParam int stock) {

        if (stock < 0) {
            return "redirect:/medicaments";
        }

        medRepo.findById(id).ifPresent(medicament -> {
            medicament.setStock(stock);
            medRepo.save(medicament);
        });

        return "redirect:/medicaments";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        medRepo.deleteById(id);
        return "redirect:/medicaments";
    }
}