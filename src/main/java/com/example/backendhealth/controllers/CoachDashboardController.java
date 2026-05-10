package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.*;
import com.example.backendhealth.entities.CoachMessage;
import com.example.backendhealth.services.CoachDashboardService;
import com.example.backendhealth.services.CoachMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach-dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CoachDashboardController {

    private final CoachDashboardService coachDashboardService;
    private final CoachMessagingService coachMessagingService;

    @GetMapping("/overview")
    public ResponseEntity<CoachDashboardOverviewDTO> overview(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachDashboardService.getOverview(resolvedCoachId));
    }

    @GetMapping("/workout-plans")
    public ResponseEntity<List<CoachWorkoutPlanSummaryDTO>> workoutPlans(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachDashboardService.getWorkoutPlans(resolvedCoachId));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<CoachDashboardClientDTO>> clients(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachDashboardService.getClients(resolvedCoachId));
    }

    @PostMapping("/assignments")
    public ResponseEntity<CoachPlanAssignmentDTO> assignPlan(@RequestBody CoachPlanAssignmentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coachDashboardService.assignPlanToClient(dto));
    }

    @PatchMapping("/assignments/{id}")
    public ResponseEntity<CoachPlanAssignmentDTO> updateProgress(
            @PathVariable Long id,
            @RequestBody CoachPlanAssignmentDTO dto) {
        return ResponseEntity.ok(coachDashboardService.updateAssignment(id, dto));
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<CoachAnalyticsItemDTO>> analytics(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachDashboardService.getAnalytics(resolvedCoachId));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<CoachNotificationDTO>> notifications(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachDashboardService.getNotifications(resolvedCoachId));
    }

    @PostMapping("/conversations")
    public ResponseEntity<CoachConversationDTO> createConversation(@RequestBody CoachConversationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coachMessagingService.createConversation(dto));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<CoachConversationDTO>> conversations(
            @RequestParam(required = false) String coachId,
            @RequestParam(required = false) String coachEmail) {
        String resolvedCoachId = coachDashboardService.resolveCoachId(coachId, coachEmail);
        return ResponseEntity.ok(coachMessagingService.getConversationsByCoach(resolvedCoachId));
    }

    @PatchMapping("/conversations/{id}/close")
    public ResponseEntity<CoachConversationDTO> closeConversation(@PathVariable Long id) {
        return ResponseEntity.ok(coachMessagingService.closeConversation(id));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<CoachMessageDTO>> messages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(coachMessagingService.getMessages(conversationId));
    }

    @PostMapping("/messages")
    public ResponseEntity<CoachMessageDTO> sendMessage(@RequestBody CoachMessageDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(coachMessagingService.sendMessage(dto));
    }

    @PatchMapping("/conversations/{conversationId}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long conversationId,
            @RequestParam CoachMessage.SenderRole readerRole) {
        coachMessagingService.markConversationAsRead(conversationId, readerRole);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/conversations/{conversationId}/unread-count")
    public ResponseEntity<Long> unreadCount(@PathVariable Long conversationId) {
        return ResponseEntity.ok(coachMessagingService.countUnread(conversationId));
    }
}
