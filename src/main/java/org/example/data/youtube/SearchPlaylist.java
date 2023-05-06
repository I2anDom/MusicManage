package org.example.data.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;

import static org.example.data.youtube.YoutubeConstants.API_KEY;

public class SearchPlaylist {
    public static PlaylistItemListResponse searchPlaylist(String link, YouTube youtube){
        try{
            String playlistId = link.split("list=")[1];
            if(playlistId != null && !playlistId.isEmpty()){
                PlaylistItemListResponse response = youtube.playlistItems().list("snippet")
                        .setMaxResults(50L)
                        .setPlaylistId(playlistId)
                        .execute();

                response.getItems().forEach(i -> System.out.println(i.getSnippet().getTitle()));
                return response;
            }
        }catch (IOException e){
            e.getMessage();
        }
        return null;
    }
}
