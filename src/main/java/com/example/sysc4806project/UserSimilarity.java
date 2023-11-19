package com.example.sysc4806project;

import jakarta.persistence.*;

@Entity
public class UserSimilarity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id_2")
    private User user2;

    private double similarityScore;
}
