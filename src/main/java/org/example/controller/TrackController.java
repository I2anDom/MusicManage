package org.example.controller;

//import org.example.service.TrackService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {
//    TrackService trackService;
//    @DeleteMapping("/{index}")
//    public List<Track> deleteTrack(@PathVariable int index) {
////        List<Track> trackList = trackService.getTrackList();
//        trackList.remove(index);
//        return trackList;
//    }
}
