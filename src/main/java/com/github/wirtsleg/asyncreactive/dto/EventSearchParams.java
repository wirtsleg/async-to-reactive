package com.github.wirtsleg.asyncreactive.dto;

import lombok.Data;

@Data
public class EventSearchParams {
    private String query;
    private int from;
    private int size;
}
