package com.example.backendhealth.services;

import com.example.backendhealth.dto.ExerciceDto;
import com.example.backendhealth.dto.PlanExerciceDto;
import com.example.backendhealth.entities.PlanExercice;
import com.example.backendhealth.repositories.PlanExerciceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanExerciceService {

    private final PlanExerciceRepository planExerciceRepository;
    private final ExerciceService exerciceService;
    private final com.example.backendhealth.repositories.CoachPlanAssignmentRepository coachPlanAssignmentRepository;
    private final com.example.backendhealth.repositories.ExerciceRepository exerciceRepository;

    public List<PlanExerciceDto> getAll() {
        return planExerciceRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public PlanExerciceDto getById(Long id) {
        PlanExercice plan = planExerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan non trouvé avec l'id : " + id));
        return toDto(plan);
    }

    public List<PlanExerciceDto> getPlansByUserId(String userId) {
        return coachPlanAssignmentRepository.findByClientId(userId).stream()
                .map(assignment -> planExerciceRepository.findById(assignment.getPlanExerciceId()))
                .filter(java.util.Optional::isPresent)
                .map(opt -> toDto(opt.get()))
                .collect(Collectors.toList());
    }

    public PlanExerciceDto create(PlanExerciceDto dto) {
        PlanExercice plan = planExerciceRepository.save(toEntity(dto));
        if (dto.getExercices() != null && !dto.getExercices().isEmpty()) {
            associateExercises(plan, dto.getExercices());
        }
        return toDto(planExerciceRepository.findById(plan.getId()).get());
    }

    public PlanExerciceDto update(Long id, PlanExerciceDto dto) {
        PlanExercice plan = planExerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan non trouvé avec l'id : " + id));

        plan.setNom(dto.getNom());
        plan.setDescription(dto.getDescription());
        plan.setDureeSemaines(dto.getDureeSemaines());
        plan.setSeancesParSemaine(dto.getSeancesParSemaine());
        plan.setActif(dto.getActif());
        plan.setDateDebut(dto.getDateDebut());
        plan.setDateFin(dto.getDateFin());
        if (dto.getCoachId() != null) plan.setCoachId(dto.getCoachId());

        PlanExercice savedPlan = planExerciceRepository.save(plan);
        if (dto.getExercices() != null) {
            associateExercises(savedPlan, dto.getExercices());
        }
        return toDto(planExerciceRepository.findById(savedPlan.getId()).get());
    }

    private void associateExercises(PlanExercice plan, List<ExerciceDto> exercises) {
        for (ExerciceDto exDto : exercises) {
            if (exDto.getId() != null) {
                // Existing exercise: re-assign to this plan
                com.example.backendhealth.entities.Exercice exercise = exerciceRepository.findById(exDto.getId())
                        .orElseThrow(() -> new RuntimeException("Exercice non trouvé: " + exDto.getId()));
                exercise.setPlanExercice(plan);
                exerciceRepository.save(exercise);
            } else {
                // New exercise: create and assign
                exDto.setPlanExerciceId(plan.getId());
                exerciceService.create(exDto);
            }
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void delete(Long id) {
        if (!planExerciceRepository.existsById(id))
            throw new RuntimeException("Plan non trouvé avec l'id : " + id);
        
        // Supprimer les assignations d'abord
        coachPlanAssignmentRepository.deleteByPlanExerciceId(id);
        
        planExerciceRepository.deleteById(id);
    }

    private PlanExerciceDto toDto(PlanExercice p) {
        List<ExerciceDto> exerciceDtos = p.getExercices()
                .stream().map(exerciceService::toDto).collect(Collectors.toList());

        return PlanExerciceDto.builder()
                .id(p.getId())
                .nom(p.getNom())
                .description(p.getDescription())
                .dureeSemaines(p.getDureeSemaines())
                .seancesParSemaine(p.getSeancesParSemaine())
                .actif(p.getActif())
                .coachId(p.getCoachId())
                .dateDebut(p.getDateDebut())
                .dateFin(p.getDateFin())
                .exercices(exerciceDtos)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private PlanExercice toEntity(PlanExerciceDto dto) {
        return PlanExercice.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .dureeSemaines(dto.getDureeSemaines())
                .seancesParSemaine(dto.getSeancesParSemaine())
                .actif(dto.getActif() != null ? dto.getActif() : true)
                .coachId(dto.getCoachId())
                .dateDebut(dto.getDateDebut() != null ? dto.getDateDebut() : java.time.LocalDate.now())
                .dateFin(dto.getDateFin())
                .build();
    }
}