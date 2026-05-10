package com.example.backendhealth.controllers;

import com.example.backendhealth.entities.Repas;
import com.example.backendhealth.services.RepasService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repas")
public class RepasController {

    private final RepasService repasService;

    public RepasController(RepasService repasService) {
        this.repasService = repasService;
    }

    @GetMapping
    public List<Repas> getAll() {
        return repasService.getAll();
    }

    @PostMapping
    public Repas create(@RequestBody Repas repas) {
        return repasService.save(repas);
    }
}