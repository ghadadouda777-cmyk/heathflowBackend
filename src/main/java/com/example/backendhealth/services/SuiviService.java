package com.example.backendhealth.services;

import com.example.backendhealth.entities.Suivi_quotidien;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.SuiviRepository;
import com.example.backendhealth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class SuiviService {

    private final SuiviRepository repository;
    private final UserRepository userRepository; // ← ajoute

    public SuiviService(SuiviRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    public Suivi_quotidien getSuiviDuJour(String userId) {
        LocalDate today = LocalDate.now();
        return repository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createSuivi(userId));
    }


    private Suivi_quotidien createSuivi(String userId) {
        user u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));

        Suivi_quotidien suivi = new Suivi_quotidien();
        suivi.setUser(u);
        suivi.setDate(LocalDate.now());
        suivi.setNb_coupes_bues(0);
        suivi.setNb_heures_sommeil(0);
        suivi.setCalories_consommes(0);
        suivi.setProteines_consommes(0);


        suivi.setNb_exercices_faites(getExoSemaineCourante(userId));

        return repository.save(suivi);
    }

    private int getExoSemaineCourante(String userId) {
        LocalDate lundi = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate hier  = LocalDate.now().minusDays(1);

        return repository.findByUserIdAndDateBetween(userId, lundi, hier)
                .stream()
                .mapToInt(s -> s.getNb_exercices_faites() == null ? 0 : s.getNb_exercices_faites())
                .sum();
    }

    public Suivi_quotidien incrementEau(String userId) {
        Suivi_quotidien suivi = getSuiviDuJour(userId);
        suivi.setNb_coupes_bues(suivi.getNb_coupes_bues() + 1);
        return repository.save(suivi);
    }

    public Suivi_quotidien incrementExercice(String userId) {
        Suivi_quotidien suivi = getSuiviDuJour(userId);
        suivi.setNb_exercices_faites(suivi.getNb_exercices_faites() + 1);
        return repository.save(suivi);
    }

    public Suivi_quotidien updateSommeil(String userId, Integer heures) {
        Suivi_quotidien suivi = getSuiviDuJour(userId);
        suivi.setNb_heures_sommeil(heures);
        return repository.save(suivi);
    }

    public Suivi_quotidien updateCalories(String userId, Integer cal) {
        Suivi_quotidien suivi = getSuiviDuJour(userId);
        int current = suivi.getCalories_consommes() == null ? 0 : suivi.getCalories_consommes();
        suivi.setCalories_consommes(current + cal);
        return repository.save(suivi);
    }

    public Suivi_quotidien updateProteines(String userId, Integer prot) {
        Suivi_quotidien suivi = getSuiviDuJour(userId);
        int current = suivi.getProteines_consommes() == null ? 0 : suivi.getProteines_consommes();
        suivi.setProteines_consommes(current + prot);
        return repository.save(suivi);
    }
}