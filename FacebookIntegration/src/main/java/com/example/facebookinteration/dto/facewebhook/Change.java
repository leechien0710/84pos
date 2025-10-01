package com.example.facebookinteration.dto.facewebhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Change {
    private String field;
    private Map<String,Object> value;
}
