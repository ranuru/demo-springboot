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

    @ManyToOne
    private User user;

    private long userId;
    private long pollId;


    @ManyToOne
    private Poll poll;

    @ManyToOne
    private VoteOption votesOn;

    private long voteOptionId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long voteId;
}
