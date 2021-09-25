package com.github.wirtsleg.asyncreactive.dto;

import lombok.Data;

@Data
public class Event implements Document {
    private String id;
    private String name;
    private long timestamp;
}
