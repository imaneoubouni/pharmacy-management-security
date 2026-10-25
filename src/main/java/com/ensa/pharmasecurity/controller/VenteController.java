package com.ensa.pharmasecurity.controller;

import com.ensa.pharmasecurity.model.Medicament;
import com.ensa.pharmasecurity.repository.MedicamentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ventes")
public class VenteController {

    private final MedicamentRepository medRepo;

    public VenteController(MedicamentRepository medRepo) {
        this.medRepo = medRepo;
    }

    @GetMapping
    public String formulaire(Model model) {
        model.addAttribute("medicaments", medRepo.findAll());
        return "ventes";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(
            @RequestParam Long medicamentId,
            @RequestParam int quantite,
            RedirectAttributes ra) {

        Medicament medicament =
            medRepo.findById(medicamentId).orElse(null);

        if (medicament == null || quantite <= 0) {
            ra.addFlashAttribute("erreur", "Donnees invalides.");
        } else if (medicament.getStock() < quantite) {
            ra.addFlashAttribute("erreur", "Stock insuffisant.");
        } else {
            medicament.setStock(
                medicament.getStock() - quantite);

            medRepo.save(medicament);

            ra.addFlashAttribute(
                "succes",
                "Vente enregistree : " + quantite
                + " x " + medicament.getNom()
                + " = " + (quantite * medicament.getPrix())
                + " DH"
            );
        }

        return "redirect:/ventes";
    }
}