package com.softserve.teachua.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class StudioResponse {

    private Long id;
    private String name;
}
