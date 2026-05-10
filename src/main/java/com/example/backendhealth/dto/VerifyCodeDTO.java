package com.example.backendhealth.dto;

import lombok.Data;

@Data
public class VerifyCodeDTO {
    private String email;
    private String code;
}
