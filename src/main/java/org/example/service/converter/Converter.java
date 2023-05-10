package org.example.service.converter;

import com.google.api.services.youtube.model.SearchResult;
import org.example.data.spotify.CreatePlaylist;
import org.example.data.spotify.SearchItem;
import org.example.data.spotify.SpotifyDataAPI;
import org.example.data.youtube.SearchPlaylist;
import org.example.data.youtube.YoutubeDataAPI;
import org.example.dto.ImageToTextDTO;
import org.example.dto.ImageToTextNanonetsDTO.Root;
import org.example.dto.ImageToTextNanonetsDTO.Word;
import org.example.dto.ItemToSearchDTO;
import org.example.service.ImageConverter.ImageConverter;
import org.springframework.web.multipart.MultipartFile;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


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

    public static void fromScreenshotsToPlaylist(List<MultipartFile> multipartFileList) throws IOException {
        List<Root> roots = ImageConverter.makeRequest(multipartFileList);
        List<ItemToSearchDTO> itemToSearchDTOList = ImageConverter.getItemToSearchFromRoots(roots);
    }
}
