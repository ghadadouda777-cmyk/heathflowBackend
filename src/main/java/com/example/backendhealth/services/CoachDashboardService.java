package com.example.backendhealth.services;

import com.example.backendhealth.dto.*;
import com.example.backendhealth.entities.CoachMessage.SenderRole;
import com.example.backendhealth.entities.CoachPlanAssignment;
import com.example.backendhealth.entities.PlanExercice;
import com.example.backendhealth.entities.user;
import com.example.backendhealth.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachDashboardService {

    private final CoachPlanAssignmentRepository assignmentRepository;
    private final CoachMessageRepository coachMessageRepository;
    private final PlanExerciceRepository planExerciceRepository;
    private final ExerciceRepository exerciceRepository;
    private final UserRepository userRepository;
    private final AbonnementService abonnementService;

    public String resolveCoachId(String coachId, String coachEmail) {
        if (coachId != null && !coachId.isBlank()) {
            getUserById(coachId);
            return coachId;
        }
        if (coachEmail != null && !coachEmail.isBlank()) {
            return userRepository.findByEmail(coachEmail)
                    .map(user::getId)
                    .orElseThrow(() -> new RuntimeException("Coach introuvable pour cet email"));
        }
        throw new RuntimeException("coachId ou coachEmail est requis");
    }

    public CoachDashboardOverviewDTO getOverview(String coachId) {
        user coach = getUserById(coachId);
        AbonnementDTO.StatutAbonnementResponse sub = abonnementService.verifierStatut(coach.getEmail());

        long activePlans = planExerciceRepository.findByCoachIdAndActif(coachId, true).size();
        long totalExercises = exerciceRepository.countByCoachId(coachId);

        return CoachDashboardOverviewDTO.builder()
                .totalClients(assignmentRepository.findByCoachId(coachId).stream()
                        .map(CoachPlanAssignment::getClientId)
                        .distinct()
                        .count())
                .activeWorkoutPlans(activePlans)
                .totalExercises(totalExercises)
                .unreadMessages(coachMessageRepository.countByConversationCoachIdAndIsReadFalseAndSenderRole(coachId, SenderRole.CLIENT))
                .subscriptionStatus(sub.isHasActiveSubscription() ? "Active" : "Expired")
                .subscriptionRemainingDays(sub.getJoursRestants())
                .build();
    }

    public List<CoachWorkoutPlanSummaryDTO> getWorkoutPlans(String coachId) {
        return planExerciceRepository.findByCoachId(coachId)
                .stream()
                .sorted(Comparator.comparing(PlanExercice::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(plan -> CoachWorkoutPlanSummaryDTO.builder()
                        .id(plan.getId())
                        .nom(plan.getNom())
                        .dureeSemaines(plan.getDureeSemaines())
                        .seancesParSemaine(plan.getSeancesParSemaine())
                        .assignedClientsCount(assignmentRepository.countByCoachIdAndPlanExerciceId(coachId, plan.getId()))
                        .exercisesCount((int) exerciceRepository.countByPlanExerciceId(plan.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<CoachDashboardClientDTO> getClients(String coachId) {
        Map<String, PlanExercice> planById = planExerciceRepository.findAll().stream()
                .collect(Collectors.toMap(p -> String.valueOf(p.getId()), Function.identity()));

        List<CoachPlanAssignment> assignments = assignmentRepository.findByCoachId(coachId);
        Map<String, List<CoachPlanAssignment>> byClient = assignments.stream()
                .collect(Collectors.groupingBy(CoachPlanAssignment::getClientId));

        // Only return clients who have at least one assignment with this coach
        return byClient.entrySet().stream()
                .map(entry -> {
                    String clientId = entry.getKey();
                    List<CoachPlanAssignment> clientAssignments = entry.getValue();

                    user client = userRepository.findById(clientId).orElse(null);
                    if (client == null) return null;

                    List<String> plans = clientAssignments.stream()
                            .map(a -> planById.get(String.valueOf(a.getPlanExerciceId())))
                            .filter(Objects::nonNull)
                            .map(PlanExercice::getNom)
                            .distinct()
                            .collect(Collectors.toList());

                    String progress = clientAssignments.stream()
                            .map(CoachPlanAssignment::getProgressStatus)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse("Not Assigned");

                    return CoachDashboardClientDTO.builder()
                            .id(client.getId())
                            .name(client.getPrenom() + " " + client.getNom())
                            .email(client.getEmail())
                            .assignedPlans(plans)
                            .progressStatus(progress)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public CoachPlanAssignmentDTO assignPlanToClient(CoachPlanAssignmentDTO dto) {
        planExerciceRepository.findById(dto.getPlanExerciceId())
                .orElseThrow(() -> new RuntimeException("Plan exercice introuvable"));
        getUserById(dto.getCoachId());
        getUserById(dto.getClientId());

        assignmentRepository.findByCoachIdAndClientIdAndPlanExerciceId(dto.getCoachId(), dto.getClientId(), dto.getPlanExerciceId())
                .ifPresent(a -> { throw new RuntimeException("Ce plan est déjà affecté à ce client"); });

        CoachPlanAssignment saved = assignmentRepository.save(
                CoachPlanAssignment.builder()
                        .coachId(dto.getCoachId())
                        .clientId(dto.getClientId())
                        .planExerciceId(dto.getPlanExerciceId())
                        .progressStatus(dto.getProgressStatus() == null ? "On Track" : dto.getProgressStatus())
                        .build()
        );
        return toAssignmentDTO(saved);
    }

    @Transactional
    public CoachPlanAssignmentDTO updateAssignment(Long id, CoachPlanAssignmentDTO dto) {
        CoachPlanAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment introuvable"));
        assignment.setProgressStatus(dto.getProgressStatus() == null ? assignment.getProgressStatus() : dto.getProgressStatus());
        return toAssignmentDTO(assignmentRepository.save(assignment));
    }

    public List<CoachAnalyticsItemDTO> getAnalytics(String coachId) {
        long totalClients = Math.max(assignmentRepository.findByCoachId(coachId).stream().map(CoachPlanAssignment::getClientId).distinct().count(), 1);
        long activePlans = Math.max(planExerciceRepository.findByActif(true).size(), 1);
        long totalPlans = Math.max(planExerciceRepository.count(), 1);
        long totalExercises = Math.max(exerciceRepository.count(), 1);

        int clientActivity = (int) Math.min(100, Math.round((totalClients * 100.0) / Math.max(userRepository.findByRole("BLOOMER").size(), 1)));
        int planUsage = (int) Math.min(100, Math.round((activePlans * 100.0) / totalPlans));
        int exercisePopularity = (int) Math.min(100, Math.round((assignmentRepository.countByCoachId(coachId) * 100.0) / totalExercises));

        return List.of(
                CoachAnalyticsItemDTO.builder().label("Client Activity").value(clientActivity + "%").trend("+6% this week").fillPercent(clientActivity).build(),
                CoachAnalyticsItemDTO.builder().label("Plans Usage").value(planUsage + "%").trend("+4% this week").fillPercent(planUsage).build(),
                CoachAnalyticsItemDTO.builder().label("Exercise Popularity").value(exercisePopularity + "%").trend("+8% this week").fillPercent(exercisePopularity).build()
        );
    }

    public List<CoachNotificationDTO> getNotifications(String coachId) {
        long unreadMessages = coachMessageRepository.countByConversationCoachIdAndIsReadFalseAndSenderRole(coachId, SenderRole.CLIENT);
        long newAssignments = assignmentRepository.findByCoachId(coachId).stream().limit(5).count();
        long newClients = getClients(coachId).stream().filter(c -> c.getAssignedPlans().isEmpty()).count();
        CoachDashboardOverviewDTO overview = getOverview(coachId);

        return List.of(
                CoachNotificationDTO.builder().type("Messages").text(unreadMessages + " unread client messages").build(),
                CoachNotificationDTO.builder().type("Clients").text(newClients + " clients are waiting for plan assignment").build(),
                CoachNotificationDTO.builder().type("Plans").text(newAssignments + " plan assignments available to review").build(),
                CoachNotificationDTO.builder().type("Subscription").text("Subscription is " + overview.getSubscriptionStatus() + " (" + overview.getSubscriptionRemainingDays() + " days left)").build()
        );
    }

    private CoachPlanAssignmentDTO toAssignmentDTO(CoachPlanAssignment assignment) {
        return CoachPlanAssignmentDTO.builder()
                .id(assignment.getId())
                .coachId(assignment.getCoachId())
                .clientId(assignment.getClientId())
                .planExerciceId(assignment.getPlanExerciceId())
                .progressStatus(assignment.getProgressStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }

    private user getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + userId));
    }
}
