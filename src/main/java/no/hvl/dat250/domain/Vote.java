package no.hvl.dat250.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
public class Vote {
    private Instant publishedAt;
    private long userId;
    private long pollId;
    @ManyToOne
    @JoinColumn(name = "vote_option")
    private VoteOption votesOn;
    private long voteOptionId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long voteId;
}
