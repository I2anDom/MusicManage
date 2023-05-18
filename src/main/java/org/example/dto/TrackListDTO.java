package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrackListDTO {
    private List<Track> trackList;
}
