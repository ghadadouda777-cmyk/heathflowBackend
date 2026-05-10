package com.example.backendhealth.services;

import com.example.backendhealth.dto.ExerciceDto;
import com.example.backendhealth.entities.Exercice;
import com.example.backendhealth.entities.PlanExercice;
import com.example.backendhealth.repositories.ExerciceRepository;
import com.example.backendhealth.repositories.PlanExerciceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final PlanExerciceRepository planExerciceRepository;

    public List<ExerciceDto> getAll() {
        return exerciceRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExerciceDto getById(Long id) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé avec l'id : " + id));
        return toDto(exercice);
    }

    public List<ExerciceDto> getByPlanId(Long planId) {
        return exerciceRepository.findByPlanExerciceId(planId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ExerciceDto> getByCoachId(String coachId) {
        return exerciceRepository.findByCoachId(coachId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ExerciceDto create(ExerciceDto dto) {
        PlanExercice plan = null;
        if (dto.getPlanExerciceId() != null) {
            plan = planExerciceRepository.findById(dto.getPlanExerciceId())
                .orElseThrow(() -> new RuntimeException("Plan non trouvé avec l'id : " + dto.getPlanExerciceId()));
        }
        return toDto(exerciceRepository.save(toEntity(dto, plan)));
    }

    public ExerciceDto update(Long id, ExerciceDto dto) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé avec l'id : " + id));
        
        PlanExercice plan = null;
        if (dto.getPlanExerciceId() != null) {
            plan = planExerciceRepository.findById(dto.getPlanExerciceId())
                .orElseThrow(() -> new RuntimeException("Plan non trouvé avec l'id : " + dto.getPlanExerciceId()));
        }

        exercice.setNom(dto.getNom());
        exercice.setDescription(dto.getDescription());
        exercice.setSeries(dto.getSeries());
        exercice.setRepetitions(dto.getRepetitions());
        exercice.setDureeSecondes(dto.getDureeSecondes());
        exercice.setTempsReposSecondes(dto.getTempsReposSecondes());
        exercice.setPoidsKg(dto.getPoidsKg());
        exercice.setPlanExercice(plan);
        if (dto.getCoachId() != null) exercice.setCoachId(dto.getCoachId());

        return toDto(exerciceRepository.save(exercice));
    }

    public void delete(Long id) {
        if (!exerciceRepository.existsById(id))
            throw new RuntimeException("Exercice non trouvé avec l'id : " + id);
        exerciceRepository.deleteById(id);
    }

    public ExerciceDto toDto(Exercice e) {
        return ExerciceDto.builder()
                .id(e.getId())
                .nom(e.getNom())
                .description(e.getDescription())
                .series(e.getSeries())
                .repetitions(e.getRepetitions())
                .dureeSecondes(e.getDureeSecondes())
                .tempsReposSecondes(e.getTempsReposSecondes())
                .poidsKg(e.getPoidsKg())
                .coachId(e.getCoachId())
                .planExerciceId(e.getPlanExercice() != null ? e.getPlanExercice().getId() : null)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Exercice toEntity(ExerciceDto dto, PlanExercice plan) {
        return Exercice.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .series(dto.getSeries())
                .repetitions(dto.getRepetitions())
                .dureeSecondes(dto.getDureeSecondes())
                .tempsReposSecondes(dto.getTempsReposSecondes())
                .poidsKg(dto.getPoidsKg())
                .coachId(dto.getCoachId())
                .planExercice(plan)
                .build();
    }
}