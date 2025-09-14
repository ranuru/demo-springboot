package com.example.demo.domain;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class VoteOption {
    private long id;
    private String caption;
    private int presentationOrder;
    private int votes;
}
