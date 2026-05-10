package com.example.backendhealth.services;

import com.example.backendhealth.dto.RendezVousDTO;
import com.example.backendhealth.entities.Coach;
import com.example.backendhealth.entities.Nutritionist;
import com.example.backendhealth.entities.RendezVous;
import com.example.backendhealth.entities.RendezVous.StatutRendezVous;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.CoachRepository;
import com.example.backendhealth.repositories.NutritionistRepository;
import com.example.backendhealth.repositories.RendezVousRepository;
import com.example.backendhealth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    // ✅ Cache pour éviter N+1 queries
    private final Map<String, Nutritionist> nutriCache = new HashMap<>();
    private final Map<String, Coach>        coachCache = new HashMap<>();
    private final Map<String, user>         userCache  = new HashMap<>();

    public RendezVousService(RendezVousRepository rdvRepo,
                             NutritionistRepository nutriRepo,
                             CoachRepository coachRepo,
                             UserRepository userRepo) {
        this.rdvRepo   = rdvRepo;
        this.nutriRepo = nutriRepo;
        this.coachRepo = coachRepo;
        this.userRepo  = userRepo;
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
        return toDTO(rdvRepo.save(rdv));
    }

    public RendezVousDTO accepterRendezVous(Long id) {
        return updateStatut(id, StatutRendezVous.CONFIRME);
    }

    public RendezVousDTO refuserRendezVous(Long id) {
        return updateStatut(id, StatutRendezVous.REFUSE);
    }

    public RendezVousDTO terminerRendezVous(Long id) {
        return updateStatut(id, StatutRendezVous.TERMINE);
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