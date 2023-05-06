package org.example.data.youtube;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.example.dto.AuthorNameSongDTO;
import org.example.dto.ItemToSearchDTO;
import org.example.service.auth.youtube.auth.YoutubeAuthorization;
import org.example.service.util.YoutubeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeDataAPI {

    private static GoogleCredential CREDENTIAL;
    private static YouTube YOUTUBE;
//    private static YOUTUBE
    public static void authorizeWithCode(String code){
        if(CREDENTIAL == null){
            CREDENTIAL = YoutubeAuthorization.getCredential(code);
        }
        if(YOUTUBE == null){
            YOUTUBE = new YouTube.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), CREDENTIAL)
                    .setApplicationName("Music Transfer")
                    .build();
        }
    }

    public static void insertPlaylist(){
        try {
        PlaylistSnippet playlistSnippet = new PlaylistSnippet();
        playlistSnippet.setTitle("STEPAN LOH");
        PlaylistStatus playlistStatus = new PlaylistStatus();
        playlistStatus.setPrivacyStatus("private");

        Playlist youTubePlaylist = new Playlist();
        youTubePlaylist.setSnippet(playlistSnippet);
        youTubePlaylist.setStatus(playlistStatus);

        // Call the API to insert the new playlist. In the API call, the first
        // argument identifies the resource parts that the API response should
        // contain, and the second argument is the playlist being inserted.
        YouTube.Playlists.Insert playlistInsertCommand = null;
        playlistInsertCommand = YOUTUBE.playlists().insert("snippet,status", youTubePlaylist);
//        youTubePlaylist.getPlayer().
        Playlist playlistInserted = playlistInsertCommand.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ItemToSearchDTO> getItemToSearchDTOFromLink(String link){
        PlaylistItemListResponse response = SearchPlaylist.searchPlaylist(link, YOUTUBE);
        List<AuthorNameSongDTO> authorNameSongDTOList = YoutubeUtils.getAuthorNameSong(response);
        List<ItemToSearchDTO> itemsToSearch = new ArrayList<>();
        for(int i=0; i<response.getItems().size(); i++){
            ItemToSearchDTO itemToSearch = new ItemToSearchDTO();
            itemToSearch.setSongName(authorNameSongDTOList.get(i).getSong());
            itemToSearch.setArtist(authorNameSongDTOList.get(i).getAuthor());
            itemsToSearch.add(itemToSearch);
        }
        return itemsToSearch;
//        for(int i=0; )
//        return response.getItems().stream().map(resp -> {
//            ItemToSearchDTO item = new ItemToSearchDTO();
//            item.setSongName(resp.getSnippet().getTitle());
//
//            item.setArtist(resp.getSnippet().getResourceId().getVideoId());
//            return item;
//        }).collect(Collectors.toList());
    }

    public static YouTube getYoutubeForRequests(){
        return YOUTUBE;
    }
}
