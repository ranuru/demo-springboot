package no.hvl.dat250.messaging;

import org.springframework.stereotype.Component;

import no.hvl.dat250.dto.VoteEvent;

@Component
public class VoteEventListener {

    public void handleVoteEvent(VoteEvent message) {
        // parse the message and update PollManager
        System.out.println("Received vote: " + message);
    }
}
