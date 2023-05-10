package org.example.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageToTextDTOList {
    @SerializedName("items")
    private List<ImageToTextDTO> imageToTextDTOList;
}
