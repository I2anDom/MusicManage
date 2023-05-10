package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BoundingBox {
    @JsonProperty("x1")
    private int x1;
    @JsonProperty("x2")
    private int x2;
    @JsonProperty("y1")
    private int y1;
    @JsonProperty("y2")
    private int y2;
}
