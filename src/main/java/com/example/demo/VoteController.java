package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final PollManager pollManager;

    public VoteController (PollManager pollManager) {
        this.pollManager = pollManager;
    }

    public static class VoteRequest {
        public Long userId;
        public Long pollId;
        public Long optionId;
    }

    @PostMapping
    public ResponseEntity<Vote> addOrUpdateVote(@RequestBody VoteRequest req) {
        // anonymous voting
        User anonymous = new User();


        Optional<User> user = Optional.ofNullable(pollManager.getUsers().get(req.userId));
        Poll poll = pollManager.getPolls().get(req.pollId);

    for (VoteOption option : poll.getOptions()) {
        if (option.getId() == req.optionId && user.isPresent()) {
            pollManager.addOrUpdateVote(user.get(), poll, option);
            break;
        }
        else if (option.getId() == req.optionId) {
            pollManager.addOrUpdateVote(anonymous, poll, option);
            break;
    }
    }
    return  ResponseEntity.ok().build();
}
    @GetMapping
    public Collection<Vote> getVotes() {
        return pollManager.getVotes().values();
    }
}
