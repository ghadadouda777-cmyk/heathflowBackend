package com.example.backendhealth.services;

import com.example.backendhealth.entities.Repas;
import com.example.backendhealth.repositories.RepasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepasService {

    private final RepasRepository repasRepository;

    public RepasService(RepasRepository repasRepository) {
        this.repasRepository = repasRepository;
    }

    public List<Repas> getAll() {
        return repasRepository.findAll();
    }

    public Repas save(Repas repas) {
        return repasRepository.save(repas);
    }
}