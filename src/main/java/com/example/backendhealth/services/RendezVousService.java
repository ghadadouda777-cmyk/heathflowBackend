package com.example.backendhealth.services;

import com.example.backendhealth.dto.RendezVousDTO;
import com.example.backendhealth.entities.Coach;
import com.example.backendhealth.entities.CoachPlanAssignment;
import com.example.backendhealth.entities.Nutritionist;
import com.example.backendhealth.entities.PlanExercice;
import com.example.backendhealth.entities.RendezVous;
import com.example.backendhealth.entities.RendezVous.StatutRendezVous;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.CoachPlanAssignmentRepository;
import com.example.backendhealth.repositories.CoachRepository;
import com.example.backendhealth.repositories.NutritionistRepository;
import com.example.backendhealth.repositories.PlanExerciceRepository;
import com.example.backendhealth.repositories.RendezVousRepository;
import com.example.backendhealth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RendezVousService {

    private final RendezVousRepository rdvRepo;
    private final NutritionistRepository nutriRepo;
    private final CoachRepository coachRepo;
    private final UserRepository userRepo;
    private final CoachPlanAssignmentRepository assignmentRepo;
    private final PlanExerciceRepository planRepo;

    // ✅ Cache pour éviter N+1 queries
    private final Map<String, Nutritionist> nutriCache = new HashMap<>();
    private final Map<String, Coach>        coachCache = new HashMap<>();
    private final Map<String, user>         userCache  = new HashMap<>();

    // Optional — injected after construction to avoid circular dependency
    @Autowired(required = false)
    private NotificationService notificationService;

    public RendezVousService(RendezVousRepository rdvRepo,
                             NutritionistRepository nutriRepo,
                             CoachRepository coachRepo,
                             UserRepository userRepo,
                             CoachPlanAssignmentRepository assignmentRepo,
                             PlanExerciceRepository planRepo) {
        this.rdvRepo   = rdvRepo;
        this.nutriRepo = nutriRepo;
        this.coachRepo = coachRepo;
        this.userRepo  = userRepo;
        this.assignmentRepo = assignmentRepo;
        this.planRepo = planRepo;
    }

    public List<Nutritionist> rechercherNutritionnisteParNom(String nom) {
        return nutriRepo.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, nom);
    }

    public List<Coach> rechercherCoachParNom(String nom) {
        return coachRepo.findByNomContainingIgnoreCase(nom);
    }

    public List<Coach> getAllCoaches() {
        return coachRepo.findAll();
    }

    public List<RendezVousDTO> getAllRendezVous() {
        return rdvRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RendezVousDTO getRendezVousById(Long id) {
        return toDTO(rdvRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous introuvable : id=" + id)));
    }

    public List<RendezVousDTO> getRendezVousByUserId(String userId) {
        return rdvRepo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getRendezVousByNutritionnisteId(String nutritionnisteId) {
        return rdvRepo.findByNutritionnisteId(nutritionnisteId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getRendezVousByCoachId(String coachId) {
        return rdvRepo.findByCoachId(coachId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getRendezVousByStatut(StatutRendezVous statut) {
        return rdvRepo.findByStatut(statut).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getRendezVousByUserIdAndStatut(String userId, StatutRendezVous statut) {
        return rdvRepo.findByUserIdAndStatut(userId, statut).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getCalendrierNutritionniste(String nutritionnisteId, LocalDateTime debut, LocalDateTime fin) {
        return rdvRepo.findByNutritionnisteIdAndDateHeureBetween(nutritionnisteId, debut, fin)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<RendezVousDTO> getCalendrierCoach(String coachId, LocalDateTime debut, LocalDateTime fin) {
        return rdvRepo.findByCoachIdAndDateHeureBetween(coachId, debut, fin)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RendezVousDTO createRendezVous(RendezVousDTO dto) {
        RendezVous rdv = toEntity(dto);
        rdv.setStatut(StatutRendezVous.EN_ATTENTE);
        RendezVousDTO saved = toDTO(rdvRepo.save(rdv));

        // Notify the professional (coach or nutritionist)
        if (notificationService != null) {
            try {
                String patientName = saved.getPatientNom() != null ? saved.getPatientNom() : "Un patient";
                if (saved.getCoachId() != null) {
                    notificationService.send(
                        saved.getCoachId(), "RDV_NEW",
                        "Nouvelle demande de RDV",
                        patientName + " a demandé un rendez-vous.",
                        String.valueOf(saved.getId())
                    );
                } else if (saved.getNutritionnisteId() != null) {
                    notificationService.send(
                        saved.getNutritionnisteId(), "RDV_NEW",
                        "Nouvelle demande de RDV",
                        patientName + " a demandé un rendez-vous.",
                        String.valueOf(saved.getId())
                    );
                }
            } catch (Exception e) { /* non-fatal */ }
        }
        return saved;
    }

    public RendezVousDTO accepterRendezVous(Long id) {
        RendezVousDTO result = updateStatut(id, StatutRendezVous.CONFIRME);
        
        // ✅ Automatically create a CoachPlanAssignment (without plan) so client appears in coach's list
        if (result.getCoachId() != null && result.getUserId() != null) {
            try {
                // Check if assignment already exists
                List<CoachPlanAssignment> existing = assignmentRepo.findByCoachIdAndClientId(
                        result.getCoachId(), 
                        result.getUserId()
                );
                
                if (existing.isEmpty()) {
                    // Create assignment without plan (planExerciceId = null)
                    CoachPlanAssignment assignment = CoachPlanAssignment.builder()
                            .coachId(result.getCoachId())
                            .clientId(result.getUserId())
                            .planExerciceId(null)  // No plan yet
                            .progressStatus("Not Assigned")
                            .build();
                    assignmentRepo.save(assignment);
                }
            } catch (Exception e) {
                // Non-fatal: assignment creation failure doesn't block RDV acceptance
            }
        }
        
        if (notificationService != null && result.getUserId() != null) {
            try {
                notificationService.send(
                    result.getUserId(), "RDV_CONFIRMED",
                    "Rendez-vous confirmé",
                    "Votre rendez-vous a été confirmé.",
                    String.valueOf(result.getId())
                );
            } catch (Exception e) { /* non-fatal */ }
        }
        return result;
    }

    public RendezVousDTO refuserRendezVous(Long id) {
        RendezVousDTO result = updateStatut(id, StatutRendezVous.REFUSE);
        if (notificationService != null && result.getUserId() != null) {
            try {
                notificationService.send(
                    result.getUserId(), "RDV_REFUSED",
                    "Rendez-vous refusé",
                    "Votre rendez-vous a été refusé.",
                    String.valueOf(result.getId())
                );
            } catch (Exception e) { /* non-fatal */ }
        }
        return result;
    }

    public RendezVousDTO terminerRendezVous(Long id) {
        RendezVousDTO result = updateStatut(id, StatutRendezVous.TERMINE);
        if (notificationService != null && result.getUserId() != null) {
            try {
                notificationService.send(
                    result.getUserId(), "RDV_TERMINATED",
                    "Rendez-vous terminé",
                    "Votre rendez-vous est terminé.",
                    String.valueOf(result.getId())
                );
            } catch (Exception e) { /* non-fatal */ }
        }
        return result;
    }

    public RendezVousDTO updateStatut(Long id, StatutRendezVous statut) {
        RendezVous rdv = rdvRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous introuvable : id=" + id));
        rdv.setStatut(statut);
        return toDTO(rdvRepo.save(rdv));
    }

    public RendezVousDTO updateRendezVous(Long id, RendezVousDTO dto) {
        RendezVous existing = rdvRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous introuvable : id=" + id));
        existing.setDateHeure(dto.getDateHeure());
        existing.setStatut(dto.getStatut());
        existing.setTypeIntervenant(dto.getTypeIntervenant());
        existing.setMotif(dto.getMotif());
        existing.setNotes(dto.getNotes());
        existing.setDureeMinutes(dto.getDureeMinutes());
        existing.setUserId(dto.getUserId());
        existing.setNutritionnisteId(dto.getNutritionnisteId());
        existing.setCoachId(dto.getCoachId());
        return toDTO(rdvRepo.save(existing));
    }

    public void deleteRendezVous(Long id) {
        if (!rdvRepo.existsById(id))
            throw new EntityNotFoundException("RendezVous introuvable : id=" + id);
        rdvRepo.deleteById(id);
    }

    private RendezVousDTO toDTO(RendezVous rdv) {
        RendezVousDTO dto = new RendezVousDTO();
        dto.setId(rdv.getId());
        dto.setDateHeure(rdv.getDateHeure());
        dto.setStatut(rdv.getStatut());
        dto.setTypeIntervenant(rdv.getTypeIntervenant());
        dto.setMotif(rdv.getMotif());
        dto.setNotes(rdv.getNotes());
        dto.setDureeMinutes(rdv.getDureeMinutes());
        dto.setUserId(rdv.getUserId());
        dto.setNutritionnisteId(rdv.getNutritionnisteId());
        dto.setCoachId(rdv.getCoachId());

        // ✅ Cache nutritionniste — 1 seule requête par nutritionniste
        if (rdv.getNutritionnisteId() != null) {
            Nutritionist n = nutriCache.computeIfAbsent(
                    rdv.getNutritionnisteId(),
                    id -> nutriRepo.findById(id).orElse(null)
            );
            if (n != null) {
                dto.setNutritionnistNom(n.getNom());
                dto.setNutritionnistPrenom(n.getPrenom());
            }
        }

        // ✅ Cache coach — 1 seule requête par coach
        if (rdv.getCoachId() != null) {
            Coach c = coachCache.computeIfAbsent(
                    rdv.getCoachId(),
                    id -> coachRepo.findById(id).orElse(null)
            );
            if (c != null) {
                dto.setCoachNom(c.getNom());
                dto.setCoachPrenom(c.getPrenom());
            }
        }

        // ✅ Cache user — 1 seule requête par patient
        if (rdv.getUserId() != null) {
            user u = userCache.computeIfAbsent(
                    rdv.getUserId(),
                    id -> userRepo.findById(id).orElse(null)
            );
            if (u != null) {
                dto.setPatientNom(u.getNom() + " " + u.getPrenom());
            }
        }

        return dto;
    }

    private RendezVous toEntity(RendezVousDTO dto) {
        RendezVous rdv = new RendezVous();
        rdv.setDateHeure(dto.getDateHeure());
        rdv.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutRendezVous.EN_ATTENTE);
        rdv.setTypeIntervenant(dto.getTypeIntervenant());
        rdv.setMotif(dto.getMotif());
        rdv.setNotes(dto.getNotes());
        rdv.setDureeMinutes(dto.getDureeMinutes() != null ? dto.getDureeMinutes() : 60);
        rdv.setUserId(dto.getUserId());
        rdv.setNutritionnisteId(dto.getNutritionnisteId());
        rdv.setCoachId(dto.getCoachId());
        return rdv;
    }
}