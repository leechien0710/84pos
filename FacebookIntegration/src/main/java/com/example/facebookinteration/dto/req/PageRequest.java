package com.example.facebookinteration.dto.req;

import lombok.Data;
import java.util.List;

@Data
public class PageRequest {
    private List<String> pageIds;
}
