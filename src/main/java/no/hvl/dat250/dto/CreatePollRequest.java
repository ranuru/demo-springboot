package no.hvl.dat250.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePollRequest {
    private Long userId;
    private String question;
    private Instant validUntil;
    private List<String> options;
}
