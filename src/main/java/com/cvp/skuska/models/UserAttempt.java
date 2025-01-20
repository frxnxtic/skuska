package com.cvp.skuska.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_attempts")
public class UserAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String problem;
    private String userAnswer;
    private boolean isCorrect;

    // Getters and Setters
}
