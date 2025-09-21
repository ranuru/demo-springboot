package no.hvl.dat250.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
        option.setPresentationOrder(this.options.size());
        this.options.add(option);
        option.setPoll(this);
        return option;
    }



}
