package com.example.backendhealth.services;

import com.example.backendhealth.dto.ConsultationDTO;
import com.example.backendhealth.entities.Consultation;
import com.example.backendhealth.repositories.ConsultationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepo;

    public ConsultationService(ConsultationRepository consultationRepo) {
        this.consultationRepo = consultationRepo;
    }

    public List<ConsultationDTO> getAllConsultations() {
        return consultationRepo.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultationDTO getConsultationById(Long id) {
        return toDTO(consultationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultation introuvable : id=" + id)));
    }

    public ConsultationDTO getByRendezVousId(Long rendezVousId) {
        return toDTO(consultationRepo.findByRendezVousId(rendezVousId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Consultation introuvable pour le RDV : id=" + rendezVousId)));
    }

    public List<ConsultationDTO> getByUserId(Long userId) {
        return consultationRepo.findByUserId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultationDTO> getByNutritionnisteId(Long nutritionnisteId) {
        return consultationRepo.findByNutritionnisteId(nutritionnisteId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ConsultationDTO> getByCoachId(Long coachId) {
        return consultationRepo.findByCoachId(coachId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConsultationDTO createConsultation(ConsultationDTO dto) {
        return toDTO(consultationRepo.save(toEntity(dto)));
    }

    public ConsultationDTO updateConsultation(Long id, ConsultationDTO dto) {
        Consultation existing = consultationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultation introuvable : id=" + id));

        existing.setRendezVousId(dto.getRendezVousId());
        existing.setUserId(dto.getUserId());
        existing.setNutritionnisteId(dto.getNutritionnisteId());
        existing.setCoachId(dto.getCoachId());
        existing.setPoids(dto.getPoids());
        existing.setTaille(dto.getTaille());
        existing.setObjectif(dto.getObjectif());
        existing.setDiagnostic(dto.getDiagnostic());
        existing.setRecommandations(dto.getRecommandations());
        existing.setNotes(dto.getNotes());
        existing.setProchainRdv(dto.getProchainRdv());

        return toDTO(consultationRepo.save(existing));
    }

    public void deleteConsultation(Long id) {
        if (!consultationRepo.existsById(id))
            throw new EntityNotFoundException("Consultation introuvable : id=" + id);
        consultationRepo.deleteById(id);
    }

    private ConsultationDTO toDTO(Consultation c) {
        ConsultationDTO dto = new ConsultationDTO();
        dto.setId(c.getId());
        dto.setRendezVousId(c.getRendezVousId());
        dto.setUserId(c.getUserId());
        dto.setNutritionnisteId(c.getNutritionnisteId());
        dto.setCoachId(c.getCoachId());
        dto.setPoids(c.getPoids());
        dto.setTaille(c.getTaille());
        dto.setImc(c.getImc());
        dto.setObjectif(c.getObjectif());
        // Récupère l'ID du premier plan lié s'il existe
        if (c.getPlansAlimentaires() != null && !c.getPlansAlimentaires().isEmpty()) {
            dto.setPlanAlimentaireId(c.getPlansAlimentaires().get(0).getId());
        }
        dto.setDiagnostic(c.getDiagnostic());
        dto.setRecommandations(c.getRecommandations());
        dto.setNotes(c.getNotes());
        dto.setDateConsultation(c.getDateConsultation());
        dto.setProchainRdv(c.getProchainRdv());
        return dto;
    }

    private Consultation toEntity(ConsultationDTO dto) {
        Consultation c = new Consultation();
        c.setRendezVousId(dto.getRendezVousId());
        c.setUserId(dto.getUserId());
        c.setNutritionnisteId(dto.getNutritionnisteId());
        c.setCoachId(dto.getCoachId());
        c.setPoids(dto.getPoids());
        c.setTaille(dto.getTaille());
        c.setObjectif(dto.getObjectif());
        c.setDiagnostic(dto.getDiagnostic());
        c.setRecommandations(dto.getRecommandations());
        c.setNotes(dto.getNotes());
        c.setProchainRdv(dto.getProchainRdv());
        return c;
    }
}