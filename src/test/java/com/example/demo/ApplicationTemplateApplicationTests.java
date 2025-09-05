package com.example.demo;

import com.example.demo.domain.Poll;
import com.example.demo.domain.User;
import com.example.demo.domain.Vote;
import com.example.demo.dto.CreatePollRequest;
import com.example.demo.dto.VoteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTemplateApplicationTests {

	@Test
	void contextLoads() {
	}


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testScenario() {
        // 1. Create user 1
        User user1 = new User();
        user1.setUsername("alice");
        user1.setEmail("alice@example.com");
        ResponseEntity<User> createdUser1 = restTemplate.postForEntity("/users", user1, User.class);

        // 2. List users (should show alice)
        ResponseEntity<User[]> usersResponse = restTemplate.getForEntity("/users", User[].class);
        assertThat(usersResponse.getBody()).extracting("username").contains("alice");

        // 3. Create user 2
        User user2 = new User();
        user2.setUsername("bob");
        user2.setEmail("bob@example.com");
        restTemplate.postForEntity("/users", user2, User.class);

        // 4. List users (should show alice & bob)
        usersResponse = restTemplate.getForEntity("/users", User[].class);
        assertThat(usersResponse.getBody()).extracting("username").containsExactlyInAnyOrder("alice", "bob");

        // 5. User 1 creates a poll
        CreatePollRequest pollRequest = new CreatePollRequest();
        pollRequest.setQuestion("Is Java fun?");
        pollRequest.setOptions(List.of("Yes", "No"));
        Assertions.assertNotNull(createdUser1.getBody());
        pollRequest.setUserId(createdUser1.getBody().getId());
        pollRequest.setValidUntil(Instant.now());
        ResponseEntity<Poll> createdPoll = restTemplate.postForEntity("/polls", pollRequest, Poll.class);

        // 6. List polls (should include new poll)
        ResponseEntity<Poll[]> pollsResponse = restTemplate.getForEntity("/polls", Poll[].class);
        assertThat(pollsResponse.getBody()).extracting("question").contains("Is Java fun?");

        // 7. User 2 votes
        VoteRequest voteReq = new VoteRequest();
        voteReq.setUserId(user2.getId());
        Assertions.assertNotNull(createdPoll.getBody());
        voteReq.setPollId(createdPoll.getBody().getId());
        voteReq.setOptionId(1L);

        restTemplate.postForEntity("/votes", voteReq, Vote.class);

        // 8. User 2 changes vote
        VoteRequest newVoteReq = new VoteRequest();
        newVoteReq.setUserId(user2.getId());
        newVoteReq.setPollId(createdPoll.getBody().getId());
        newVoteReq.setOptionId(2L);
        restTemplate.postForEntity("/votes", newVoteReq, Vote.class);

        // 9. List votes (should show most recent = "No")
        ResponseEntity<Vote[]> votesResponse = restTemplate.getForEntity("/votes", Vote[].class);
        assertThat(votesResponse.getBody()).extracting("voteOptionId").contains(2L);

        // 10. Delete poll
        restTemplate.delete("/polls/{id}", createdPoll.getBody().getId());

        // 11. List votes (should be empty)
        votesResponse = restTemplate.getForEntity("/votes", Vote[].class);
        assertThat(votesResponse.getBody()).isEmpty();
    }

}
