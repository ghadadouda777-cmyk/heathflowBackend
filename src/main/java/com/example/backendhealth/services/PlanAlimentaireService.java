package com.example.backendhealth.services;

import com.example.backendhealth.dto.PlanAlimentaireDTO;
import com.example.backendhealth.dto.RepasDTO;
import com.example.backendhealth.entities.Consultation;
import com.example.backendhealth.entities.PlanAlimentaire;
import com.example.backendhealth.entities.Repas;
import com.example.backendhealth.repositories.ConsultationRepository;
import com.example.backendhealth.repositories.PlanAlimentaireRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanAlimentaireService {

    private final PlanAlimentaireRepository planRepo;
    private final ConsultationRepository consultationRepo;

    public PlanAlimentaireService(PlanAlimentaireRepository planRepo,
                                  ConsultationRepository consultationRepo) {
        this.planRepo = planRepo;
        this.consultationRepo = consultationRepo;
    }

    public List<PlanAlimentaireDTO> getAllPlans() {
        return planRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PlanAlimentaireDTO getPlanById(Long id) {
        return toDTO(planRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan introuvable : id=" + id)));
    }

    // ← Optional — ne lance plus d'exception si pas de plan
    public Optional<PlanAlimentaireDTO> getPlanByConsultationId(Long consultationId) {
        return planRepo.findByConsultationId(consultationId).map(this::toDTO);
    }

    public List<PlanAlimentaireDTO> getPlansByUserId(String userId)
 {
        return planRepo.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PlanAlimentaireDTO> getPlansByRegimeId(Long regimeId) {
        return planRepo.findByRegimeId(regimeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PlanAlimentaireDTO createPlan(PlanAlimentaireDTO dto) {
        PlanAlimentaire plan = toEntity(dto);

        // ← ifPresent — pas d'exception si consultation introuvable
        if (dto.getConsultationId() != null) {
            consultationRepo.findById(dto.getConsultationId())
                    .ifPresent(plan::setConsultation);
        }

        if (dto.getRepas() != null && !dto.getRepas().isEmpty()) {
            List<Repas> repasList = dto.getRepas().stream()
                    .map(r -> repasToEntity(r, plan))
                    .collect(Collectors.toList());
            plan.setRepas(repasList);
        }

        return toDTO(planRepo.save(plan));
    }

    public PlanAlimentaireDTO updatePlan(Long id, PlanAlimentaireDTO dto) {
        PlanAlimentaire existing = planRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan introuvable : id=" + id));

        existing.setNom(dto.getNom());
        existing.setDescription(dto.getDescription());
        existing.setDateDebut(dto.getDateDebut());
        existing.setDateFin(dto.getDateFin());
        existing.setCaloriesJournalieres(dto.getCaloriesJournalieres());
        existing.setObjectif(dto.getObjectif());
        existing.setUserId(dto.getUserId());
        existing.setNutritionnisteId(dto.getNutritionnisteId());
        existing.setRegimeId(dto.getRegimeId());

        if (dto.getConsultationId() != null) {
            consultationRepo.findById(dto.getConsultationId())
                    .ifPresent(existing::setConsultation);
        }

        if (dto.getRepas() != null) {
            existing.getRepas().clear();
            List<Repas> updatedRepas = dto.getRepas().stream()
                    .map(r -> repasToEntity(r, existing))
                    .collect(Collectors.toList());
            existing.getRepas().addAll(updatedRepas);
        }

        return toDTO(planRepo.save(existing));
    }

    public void deletePlan(Long id) {
        if (!planRepo.existsById(id))
            throw new EntityNotFoundException("Plan introuvable : id=" + id);
        planRepo.deleteById(id);
    }

    // ─── Mappers ──────────────────────────────────────────────────────────────

    private PlanAlimentaireDTO toDTO(PlanAlimentaire plan) {
        PlanAlimentaireDTO dto = new PlanAlimentaireDTO();
        dto.setId(plan.getId());
        dto.setNom(plan.getNom());
        dto.setDescription(plan.getDescription());
        dto.setDateDebut(plan.getDateDebut());
        dto.setDateFin(plan.getDateFin());
        dto.setCaloriesJournalieres(plan.getCaloriesJournalieres());
        dto.setObjectif(plan.getObjectif());
        dto.setUserId(plan.getUserId());
        dto.setNutritionnisteId(plan.getNutritionnisteId());
        dto.setRegimeId(plan.getRegimeId());
        if (plan.getConsultation() != null)
            dto.setConsultationId(plan.getConsultation().getId());
        if (plan.getRepas() != null)
            dto.setRepas(plan.getRepas().stream()
                    .map(this::repasToDTO)
                    .collect(Collectors.toList()));
        return dto;
    }

    private PlanAlimentaire toEntity(PlanAlimentaireDTO dto) {
        PlanAlimentaire plan = new PlanAlimentaire();
        plan.setNom(dto.getNom());
        plan.setDescription(dto.getDescription());
        plan.setDateDebut(dto.getDateDebut());
        plan.setDateFin(dto.getDateFin());
        plan.setCaloriesJournalieres(dto.getCaloriesJournalieres());
        plan.setObjectif(dto.getObjectif());
        plan.setUserId(dto.getUserId());
        plan.setNutritionnisteId(dto.getNutritionnisteId());
        plan.setRegimeId(dto.getRegimeId());
        plan.setRepas(new ArrayList<>());
        return plan;
    }

    private RepasDTO repasToDTO(Repas r) {
        RepasDTO dto = new RepasDTO();
        dto.setId(r.getId());
        dto.setTypeRepas(r.getTypeRepas());
        dto.setNom(r.getNom());
        dto.setAliments(r.getAliments());
        dto.setCalories(r.getCalories());
        dto.setProteines(r.getProteines());
        dto.setGlucides(r.getGlucides());
        dto.setLipides(r.getLipides());
        dto.setNotes(r.getNotes());
        dto.setPlanAlimentaireId(
                r.getPlanAlimentaire() != null ? r.getPlanAlimentaire().getId() : null);
        return dto;
    }

    private Repas repasToEntity(RepasDTO dto, PlanAlimentaire plan) {
        Repas r = new Repas();
        r.setTypeRepas(dto.getTypeRepas());
        r.setNom(dto.getNom());
        r.setAliments(dto.getAliments());
        r.setCalories(dto.getCalories());
        r.setProteines(dto.getProteines());
        r.setGlucides(dto.getGlucides());
        r.setLipides(dto.getLipides());
        r.setNotes(dto.getNotes());
        r.setPlanAlimentaire(plan);
        return r;
    }
}