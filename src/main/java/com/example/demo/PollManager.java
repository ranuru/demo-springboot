package com.example.demo;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Data
@AllArgsConstructor
public class PollManager {

    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, User>  users = new HashMap<>();
    private final Map<Long, Vote>  votes = new HashMap<>();

    private final AtomicLong userIdGen = new AtomicLong(1);
    private final AtomicLong voteIdGen = new AtomicLong(1);
    private final AtomicLong pollIdGen = new AtomicLong(1);
    private final AtomicLong optionIdGen = new AtomicLong(1);

    public void createUser(String username, String email) {
        User user = new User();
        user.setId(userIdGen.getAndIncrement());
        user.setUsername(username);
        user.setEmail(email);
        users.put(user.getId(), user);
    }

    public void createPoll(User creator, String question, Instant validUntil, List<String> options) {
        Poll poll = new Poll();
        poll.setId(pollIdGen.getAndIncrement());
        poll.setPublishedAt(Instant.now());
        poll.setQuestion(question);
        poll.setValidUntil(validUntil);
        poll.setCreator(creator);

        List<VoteOption> voteOptions = new ArrayList<>();
        int presentationOrder = 0;

        for (String option : options) {
            VoteOption voteOption = new VoteOption();
            voteOption.setId(optionIdGen.getAndIncrement());
            voteOption.setCaption(option);
            voteOption.setPresentationOrder(presentationOrder);
            voteOptions.add(voteOption);

            presentationOrder++;
        }
        poll.setOptions(voteOptions);
        polls.put(poll.getId(), poll);
    }


    public User getUserFromId(Long id) {
        return users.get(id);
    }
}
