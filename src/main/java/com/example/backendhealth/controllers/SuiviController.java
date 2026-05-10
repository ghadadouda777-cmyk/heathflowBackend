package com.example.backendhealth.controllers;

import com.example.backendhealth.entities.Suivi_quotidien;
import com.example.backendhealth.services.SuiviService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suivi")
@CrossOrigin("*")
public class SuiviController {

    private final SuiviService service;

    public SuiviController(SuiviService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public Suivi_quotidien getSuiviDuJour(@PathVariable String userId) {
        return service.getSuiviDuJour(userId);
    }

    @PutMapping("/{userId}/eau")
    public Suivi_quotidien incrementEau(@PathVariable String userId) {
        return service.incrementEau(userId);
    }

    @PutMapping("/{userId}/exercice")
    public Suivi_quotidien incrementExercice(@PathVariable String userId) {
        return service.incrementExercice(userId);
    }

    @PutMapping("/{userId}/sommeil")
    public Suivi_quotidien updateSommeil(@PathVariable String userId,
                                         @RequestBody Integer heures) {
        return service.updateSommeil(userId, heures);
    }

    @PutMapping("/{userId}/calories")
    public Suivi_quotidien updateCalories(@PathVariable String userId,
                                          @RequestBody Integer cal) {
        return service.updateCalories(userId, cal);
    }

    @PutMapping("/{userId}/proteines")
    public Suivi_quotidien updateProteines(@PathVariable String userId,
                                           @RequestBody Integer prot) {
        return service.updateProteines(userId, prot);
    }
}