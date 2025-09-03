package com.example.demo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Vote {
    private Instant publishedAt;
}
