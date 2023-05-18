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
    private static List<Track> lastConvertedSpotifyList;
    private static List<SearchResult> lastConvertedYoutubeList;
    public static List<Track> fromYoutubeToSpotify(String link){
        List<ItemToSearchDTO> itemsToSearch = YoutubeDataAPI.getItemToSearchDTOFromLink(link);
        lastConvertedSpotifyList = SearchItem.searchItems(itemsToSearch);
        return lastConvertedSpotifyList;
//        CreatePlaylist.createPlaylist("testList-23958г39084192384", false, trackList);
    }
    public static List<SearchResult> fromSpotifyToYoutube(String link){
        List<ItemToSearchDTO> itemsToSearch = SpotifyDataAPI.getItemToSearchDTOFromLink(link);
        lastConvertedYoutubeList = org.example.data.youtube.SearchItem.searchItems(itemsToSearch);
        return lastConvertedYoutubeList;
//        org.example.data.youtube.CreatePlaylist.createPlaylist("testList212345", false, searchResults);
    }

    public static List<Track> fromScreenshotsToSpotify(List<MultipartFile> multipartFileList) throws IOException {
        List<Root> roots = ImageConverter.makeRequest(multipartFileList);
        List<ItemToSearchDTO> itemToSearchDTOList = ImageConverter.getItemToSearchFromRoots(roots);
        lastConvertedSpotifyList = SearchItem.searchItems(itemToSearchDTOList);
        return lastConvertedSpotifyList;
    }

    public static List<SearchResult> fromScreenshotsToYoutube(List<MultipartFile> multipartFileList) throws IOException {
        List<Root> roots = ImageConverter.makeRequest(multipartFileList);
        List<ItemToSearchDTO> itemToSearchDTOList = ImageConverter.getItemToSearchFromRoots(roots);
        lastConvertedYoutubeList = org.example.data.youtube.SearchItem.searchItems(itemToSearchDTOList);
        return lastConvertedYoutubeList;
    }

    public static List<Track> getLastConvertedSpotifyListList(){
        return lastConvertedSpotifyList;
    }

    public static List<SearchResult> getLastConvertedYoutubeList(){
        return lastConvertedYoutubeList;
    }
}
