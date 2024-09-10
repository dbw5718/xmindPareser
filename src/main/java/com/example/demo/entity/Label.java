package com.example.demo.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Label {
    private List<String> front = new ArrayList<>();
    private List<String> flag = new ArrayList<>();
    private List<String> task = new ArrayList<>();
    private List<String> back = new ArrayList<>();

}
