package org.example.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NanonetsRequestDTO {
    private List<CategoryDTO> categories;
    @JsonProperty("model_id")
    private String modelId;
    @JsonProperty("model_type")
    private String modelType;
    private int state;
}

