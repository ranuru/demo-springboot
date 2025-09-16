package no.hvl.dat250.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class CreatePollRequest {
    private Long userId;
    private String question;
    private Instant validUntil;
    private List<String> options;
}
