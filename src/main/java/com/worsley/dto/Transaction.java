package com.worsley.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Transaction(Amount amount, Direction direction) {
}
