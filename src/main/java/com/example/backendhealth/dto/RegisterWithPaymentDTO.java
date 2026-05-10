package com.example.backendhealth.dto;
import lombok.Data;

@Data
public class RegisterWithPaymentDTO {
  private RegisterDTO register;
  private AbonnementDTO.PaymentRequest payment;
}
