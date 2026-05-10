package com.example.backendhealth.controllers;

import com.example.backendhealth.dto.ForgotPasswordDTO;
import com.example.backendhealth.dto.LoginDTO;
import com.example.backendhealth.dto.RegisterDTO;
import com.example.backendhealth.dto.RegisterWithPaymentDTO;
import com.example.backendhealth.dto.ResetPasswordDTO;
import com.example.backendhealth.dto.VerifyCodeDTO;
import com.example.backendhealth.services.AuthService;
import com.example.backendhealth.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

  private final AuthService authService;
  private final PasswordResetService passwordResetService;

  // ─── REGISTER SIMPLE ──────────────────────────────────────────────────────
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
    try {
      return ResponseEntity.ok(authService.register(dto));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
        .body(Map.of("error", e.getMessage()));
    }
  }

  // ─── REGISTER + PAIEMENT ──────────────────────────────────────────────────
  @PostMapping("/register-with-payment")
  public ResponseEntity<?> registerWithPayment(@RequestBody RegisterWithPaymentDTO dto) {
    try {
      return ResponseEntity.ok(
        authService.registerWithPayment(dto.getRegister(), dto.getPayment())
      );
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
        .body(Map.of("error", e.getMessage()));
    }
  }

  // ─── LOGIN ────────────────────────────────────────────────────────────────
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
    try {
      return ResponseEntity.ok(authService.login(dto));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
        .body(Map.of("error", e.getMessage()));
    }
  }

  // ─── FORGOT PASSWORD ──────────────────────────────────────────────────────
  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
    try {
      passwordResetService.sendResetCode(dto.getEmail());
      return ResponseEntity.ok(Map.of("message", "Code envoyé à " + dto.getEmail()));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  // ─── VERIFY CODE ──────────────────────────────────────────────────────────
  @PostMapping("/verify-code")
  public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeDTO dto) {
    try {
      passwordResetService.verifyCode(dto.getEmail(), dto.getCode());
      return ResponseEntity.ok(Map.of("message", "Code vérifié", "valid", true));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
        .body(Map.of("error", e.getMessage(), "valid", false));
    }
  }

  // ─── RESET PASSWORD ───────────────────────────────────────────────────────
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
    try {
      passwordResetService.resetPassword(dto.getEmail(), dto.getCode(), dto.getNewPassword());
      return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }
}
