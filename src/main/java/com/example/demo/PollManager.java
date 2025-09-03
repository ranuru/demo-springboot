package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PollManager {

    private HashMap<User, Poll> polls;
    private HashMap<User, Vote> votes;


}
