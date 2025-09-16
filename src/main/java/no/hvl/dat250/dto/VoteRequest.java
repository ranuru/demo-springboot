package no.hvl.dat250.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private Long userId;
    private Long pollId;
    private Long optionId;
}
