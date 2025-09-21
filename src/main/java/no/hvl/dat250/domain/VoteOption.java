package no.hvl.dat250.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String caption;
    private int presentationOrder;
    private int votes;
    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;
}
