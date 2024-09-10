package com.example.demo.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NodeData {
    private long id;
    private String name;
    private boolean dataStream;
    private boolean componentModule;
    private int moduleId;
    private boolean isRoot=false;
    private  String image;
    private int progress;
    private Label label;

    private List<NodeData> children = new ArrayList<>();



}
