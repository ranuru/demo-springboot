package no.hvl.dat250.dto;

import lombok.Data;

@Data
public class VoteEvent {
    private Long pollId;
    private Long optionId;
    private Long userId; // optional, can be null for anonymous votes
}
