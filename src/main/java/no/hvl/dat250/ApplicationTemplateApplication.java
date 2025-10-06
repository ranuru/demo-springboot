package no.hvl.dat250;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.domain.Poll;
import no.hvl.dat250.domain.Vote;
import redis.clients.jedis.UnifiedJedis;

@RestController
@SpringBootApplication
public class ApplicationTemplateApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ApplicationTemplateApplication.class, args);
		UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

		// use case 1 keep track of logged-in users
		jedis.del("loggedIn");

		jedis.sadd("loggedIn", "alice");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.sadd("loggedIn", "bob");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.srem("loggedIn", "alice");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.sadd("loggedIn", "eve");
		System.out.println(jedis.smembers("loggedIn"));

		// use case 2 representing complex information
		jedis.hset("Poll", "id", "03ebcb7b-bd69-440b-924e-f5b7d664af7b");
		jedis.hset("Poll", "title", "Pineapple on Pizza?");

		// option 1
		jedis.hset("Poll", "options", "option1");

		jedis.hset("Options", "number", "option1");

		jedis.hset("option1", "caption", "Yes yammy");
		jedis.hset("option1", "voteCount", "269");

		// option 2
		jedis.hset("Poll", "options", "option2");

		jedis.hset("Options", "number", "option2");

		jedis.hset("option2", "caption", "Mamma mia nooooooo!");
		jedis.hset("option2", "voteCount", "268");

		// option 3
		jedis.hset("Poll", "options", "option3");

		jedis.hset("Options", "number", "option3");

		jedis.hset("option3", "caption", "I do not really care ...!");
		jedis.hset("option3", "voteCount", "42");

		// implementing cache for aggregated number of votes in each poll

		// setup /////////////////////////////////////
		Poll poll1 = new Poll();
		poll1.addVoteOption("y");
		poll1.addVoteOption("n");
		poll1.addVoteOption("m");

		List<Vote> votes1 = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Vote vote = new Vote();
			votes1.add(vote);
		}

		List<Vote> votes2 = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Vote vote = new Vote();
			votes2.add(vote);
		}

		List<Vote> votes3 = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Vote vote = new Vote();
			votes3.add(vote);
		}

		poll1.getOptions().get(0).setVotes(votes1);
		poll1.getOptions().get(1).setVotes(votes2);
		poll1.getOptions().get(2).setVotes(votes3);

		////////////////////////////////////

		jedis.close();

	}
}
