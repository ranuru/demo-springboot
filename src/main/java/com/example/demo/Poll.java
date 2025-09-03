package com.example.demo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class Poll {

private String question;
private Instant publishedAt;
private Instant validUntil;
private List<VoteOption> options;
private Long id;
private User creator;
}
