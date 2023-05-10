package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ImageToTextDTO {
    @JsonProperty("text")
    private String text;
    @SerializedName("bounding_box")
    private BoundingBox boundingBox;
}
