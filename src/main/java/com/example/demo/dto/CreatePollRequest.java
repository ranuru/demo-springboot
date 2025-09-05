package com.example.demo.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CreatePollRequest {
    private Long userId;
    private String question;
    private Instant validUntil;
    private List<String> options;
}
