package com.ensa.pharmasecurity.service;

import com.ensa.pharmasecurity.model.*;
import com.ensa.pharmasecurity.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final MedicamentRepository medRepo;
    private final PasswordEncoder encoder;

    public InitData(
            RoleRepository roleRepo,
            UserRepository userRepo,
            MedicamentRepository medRepo,
            PasswordEncoder encoder) {

        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.medRepo = medRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        for (String role : new String[]{
                "ADMIN", "PHARMACIEN", "CAISSIER", "CLIENT"}) {

            if (roleRepo.findByName(role).isEmpty()) {
                roleRepo.save(new Role(role));
            }
        }

        creerUser("admin", "admin123", "ADMIN");
        creerUser("pharma", "pharma123", "PHARMACIEN");
        creerUser("caisse", "caisse123", "CAISSIER");
        creerUser("client", "client123", "CLIENT");

        if (medRepo.count() == 0) {
            medRepo.save(new Medicament(
                null, "Amoxicilline 500mg",
                "Antibiotique", 45.00, 100));

            medRepo.save(new Medicament(
                null, "Doliprane 500mg",
                "Antalgique", 25.50, 200));

            medRepo.save(new Medicament(
                null, "Vitamine C 1000mg",
                "Complement vitaminique", 30.00, 150));

            medRepo.save(new Medicament(
                null, "Betadine Solution 10%",
                "Desinfectant externe", 18.00, 80));
        }
    }

    private void creerUser(
            String username, String password, String roleName) {

        if (userRepo.findByUsername(username).isPresent()) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.getRoles().add(
            roleRepo.findByName(roleName).orElseThrow());

        userRepo.save(user);
    }
}