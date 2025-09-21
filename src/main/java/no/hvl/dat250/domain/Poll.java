package no.hvl.dat250.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Poll {
private String question;
private Instant publishedAt;
private Instant validUntil;

@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
private List<VoteOption> options =  new ArrayList<>();

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@ManyToOne
@JoinColumn(name = "user_id")
private User createdBy;



    /**
     *
     * Adds a new option to this Poll and returns the respective
     * VoteOption object with the given caption.
     * The value of the presentationOrder field gets determined
     * by the size of the currently existing VoteOptions for this Poll.
     * I.e. the first added VoteOption has presentationOrder=0, the secondly
     * registered VoteOption has presentationOrder=1 and so on.
     */
    public VoteOption addVoteOption(String caption) {
        VoteOption option = new VoteOption();
        option.setCaption(caption);
        this.options.add(option);
        return option;
    }



}
