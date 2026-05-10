package com.example.backendhealth.services;

import com.example.backendhealth.dto.RegimeAlimentaireDTO;
import com.example.backendhealth.entities.RegimeAlimentaire;
import com.example.backendhealth.repositories.RegimeAlimentaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegimeAlimentaireService {

    private final RegimeAlimentaireRepository regimeRepo;

    public RegimeAlimentaireService(RegimeAlimentaireRepository regimeRepo) {
        this.regimeRepo = regimeRepo;
    }

    public List<RegimeAlimentaireDTO> getAllRegimes() {
        return regimeRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RegimeAlimentaireDTO getRegimeById(Long id) {
        RegimeAlimentaire regime = regimeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Régime introuvable : id=" + id));
        return toDTO(regime);
    }

    public List<RegimeAlimentaireDTO> getRegimesByUserId(Long userId) {
        return regimeRepo.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RegimeAlimentaireDTO> getRegimesByType(String type) {
        return regimeRepo.findByType(type)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RegimeAlimentaireDTO> searchByNom(String nom) {
        return regimeRepo.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RegimeAlimentaireDTO createRegime(RegimeAlimentaireDTO dto) {
        RegimeAlimentaire regime = toEntity(dto);
        return toDTO(regimeRepo.save(regime));
    }

    public RegimeAlimentaireDTO updateRegime(Long id, RegimeAlimentaireDTO dto) {
        RegimeAlimentaire existing = regimeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Régime introuvable : id=" + id));

        existing.setNom(dto.getNom());
        existing.setDescription(dto.getDescription());
        existing.setType(dto.getType());
        existing.setCaloriesRecommandees(dto.getCaloriesRecommandees());
        existing.setAlimentsAutorises(dto.getAlimentsAutorises());
        existing.setAlimentsInterdits(dto.getAlimentsInterdits());
        existing.setUserId(dto.getUserId());

        return toDTO(regimeRepo.save(existing));
    }

    public void deleteRegime(Long id) {
        if (!regimeRepo.existsById(id)) {
            throw new EntityNotFoundException("Régime introuvable : id=" + id);
        }
        regimeRepo.deleteById(id);
    }

    private RegimeAlimentaireDTO toDTO(RegimeAlimentaire regime) {
        RegimeAlimentaireDTO dto = new RegimeAlimentaireDTO();
        dto.setId(regime.getId());
        dto.setNom(regime.getNom());
        dto.setDescription(regime.getDescription());
        dto.setType(regime.getType());
        dto.setCaloriesRecommandees(regime.getCaloriesRecommandees());
        dto.setAlimentsAutorises(regime.getAlimentsAutorises());
        dto.setAlimentsInterdits(regime.getAlimentsInterdits());
        dto.setUserId(regime.getUserId());
        return dto;
    }

    private RegimeAlimentaire toEntity(RegimeAlimentaireDTO dto) {
        RegimeAlimentaire regime = new RegimeAlimentaire();
        regime.setNom(dto.getNom());
        regime.setDescription(dto.getDescription());
        regime.setType(dto.getType());
        regime.setCaloriesRecommandees(dto.getCaloriesRecommandees());
        regime.setAlimentsAutorises(dto.getAlimentsAutorises());
        regime.setAlimentsInterdits(dto.getAlimentsInterdits());
        regime.setUserId(dto.getUserId());
        return regime;
    }
}
