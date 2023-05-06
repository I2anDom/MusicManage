package org.example.service.converter;

import com.google.api.services.youtube.model.SearchResult;
import org.example.data.spotify.CreatePlaylist;
import org.example.data.spotify.SearchItem;
import org.example.data.spotify.SpotifyDataAPI;
import org.example.data.youtube.SearchPlaylist;
import org.example.data.youtube.YoutubeDataAPI;
import org.example.dto.ItemToSearchDTO;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;

public class Converter {
    public static void fromYoutubeToSpotify(String link){
        List<ItemToSearchDTO> itemsToSearch = YoutubeDataAPI.getItemToSearchDTOFromLink(link);
        List<Track> trackList = SearchItem.searchItems(itemsToSearch);
        CreatePlaylist.createPlaylist("testList-23958г39084192384", false, trackList);
    }
    public static void fromSpotifyToYoutube(String link){
        List<ItemToSearchDTO> itemsToSearch = SpotifyDataAPI.getItemToSearchDTOFromLink(link);
        List<SearchResult> searchResults = org.example.data.youtube.SearchItem.searchItems(itemsToSearch);
        org.example.data.youtube.CreatePlaylist.createPlaylist("testList212345", false, searchResults);
    }
}
