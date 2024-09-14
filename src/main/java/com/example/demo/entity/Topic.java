package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean dataStream;
    private boolean componentModule;
    private int moduleId;
    private boolean isRoot;
    private String image;
    private int progress;


    private List<String> frontLabels;


    private List<String> flagLabels;


    private List<String> taskLabels;


    private List<String> backLabels;


    @JoinColumn(name = "parent_id")
    private Topic parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Topic> children;


}
