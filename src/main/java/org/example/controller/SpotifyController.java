package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ParseException;
import org.example.data.spotify.CreatePlaylist;
import org.example.data.spotify.SpotifyDataAPI;
import org.example.data.youtube.YoutubeDataAPI;
//import org.example.service.TrackService;
import org.example.dto.MyForm;
import org.example.dto.TrackListDTO;
import org.example.service.auth.spotifyAuth.SpotifyAuthorization;
import org.example.service.auth.spotifyAuth.SpotifyLoginURI;
import org.example.service.auth.youtube.auth.YoutubeLoginURI;
import org.example.service.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/spotify")
public class SpotifyController {
//    TrackService trackService;
    @GetMapping("/auth")
    public void spotifyLogin(){
    }

    @PostMapping("/convert-from-link")
    public String convert(Model model, @RequestParam String youtubeLink){
        List<Track> trackList = Converter.fromYoutubeToSpotify(youtubeLink);
        model.addAttribute("trackList", trackList);
//        boolean isPublic = false; // Створення нового об'єкта myForm
//        model.addAttribute("isPublic", isPublic); // Додавання моделі до атрибутів
        return "SpotifyListEditor";
    }

    @GetMapping("/imageToSpotify")
    public String convert(Model model, RedirectAttributes redirectAttributes){
        if(SpotifyDataAPI.getSpotifyApi() == null && YoutubeDataAPI.getYoutubeForRequests() == null){
            redirectAttributes.addFlashAttribute("isSpotifyAndYoutubeConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect both your Spotify and Youtube accounts.");
            return "redirect:/mainPage";
        }

        if(SpotifyDataAPI.getSpotifyApi() == null){
            redirectAttributes.addFlashAttribute("isSpotifyConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect your Spotify account.");
            return "redirect:/mainPage";
        }

        if(YoutubeDataAPI.getYoutubeForRequests() == null){
            redirectAttributes.addFlashAttribute("isYoutubeConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect your Youtube account.");
            return "redirect:/mainPage";
        }
        return "searchByPicture";
    }

    @PostMapping("/upload")
    public String uploadPictures(Model model, @RequestParam("images")List<MultipartFile> multipartFileList) throws IOException, URISyntaxException {
        List<Track> trackList = Converter.fromScreenshotsToSpotify(multipartFileList);
        model.addAttribute("trackList", trackList);
        return "SpotifyListEditor";
    }

    @GetMapping("/spotifyListEditor")
    public String getSpotifyListEditor(Model model){
        model.addAttribute("trackList", Converter.getLastConvertedSpotifyListList());
        return "SpotifyListEditor";
    }

    @GetMapping("/link-form")
    public String getMainPage(Model model, RedirectAttributes redirectAttributes) throws IOException, ParseException, SpotifyWebApiException {
        if(SpotifyDataAPI.getSpotifyApi() == null && YoutubeDataAPI.getYoutubeForRequests() == null){
            redirectAttributes.addFlashAttribute("isSpotifyAndYoutubeConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect both your Spotify and Youtube accounts.");
            return "redirect:/mainPage";
        }

        if(SpotifyDataAPI.getSpotifyApi() == null){
            redirectAttributes.addFlashAttribute("isSpotifyConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect your Spotify account.");
            return "redirect:/mainPage";
        }

        if(YoutubeDataAPI.getYoutubeForRequests() == null){
            redirectAttributes.addFlashAttribute("isYoutubeConnected", false);
            redirectAttributes.addFlashAttribute("errorMessage", "You need to connect your Youtube account.");
            return "redirect:/mainPage";
        }
        URI spotifyURI = SpotifyLoginURI.authorizationCodeUri_Sync();
        URI youtubeURI = YoutubeLoginURI.getLoginURI();
        model.addAttribute("spotifyURI", spotifyURI.toString());
        model.addAttribute("youtubeURI", youtubeURI.toString());
        model.addAttribute("spotifyUsername", SpotifyDataAPI.getDisplayNameOfCurrentAuthorizedUser());
        model.addAttribute("youtubeChanel", YoutubeDataAPI.getChanelNameOfCurrentAuthorizedUser());
        return "YoutubeLinkToSpotify";
    }

    @PostMapping("/remove/{trackIndex}")
    public String removeTrack(@PathVariable("trackIndex") int trackIndex, Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Track> trackList = Converter.getLastConvertedSpotifyListList();
        trackList.remove(trackIndex);
        model.addAttribute("trackList", trackList);
        return "SpotifyListEditor";
    }

    @PostMapping("/createPlaylist")
    public String createPlaylist(Model model, @RequestParam(value = "isPublic", required = false) String isPublic, @RequestParam String playlistName){
        if(isPublic == null){
            CreatePlaylist.createPlaylist(playlistName, true,
                    Converter.getLastConvertedSpotifyListList());
        }else{
            CreatePlaylist.createPlaylist(playlistName, false,
                    Converter.getLastConvertedSpotifyListList());
        }
        return "PlaylistCreated";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        SpotifyAuthorization.logout();
        return "redirect:/mainPage";
    }
}
