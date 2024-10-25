package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String description;
    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CaseView> views;
}


