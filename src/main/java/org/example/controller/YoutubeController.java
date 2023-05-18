package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.SearchResult;
import org.apache.hc.core5.http.ParseException;
import org.example.data.spotify.CreatePlaylist;
import org.example.data.spotify.SpotifyDataAPI;
import org.example.data.youtube.YoutubeDataAPI;
import org.example.service.auth.spotifyAuth.SpotifyAuthorization;
import org.example.service.auth.spotifyAuth.SpotifyLoginURI;
import org.example.service.auth.youtube.auth.YoutubeAuthorization;
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
@RequestMapping("/youtube")
public class YoutubeController {
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
        return "SpotifyLinkToYoutube";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "mainPage";
    }

    @PostMapping("/convert-from-link")
    public String convert(Model model, @RequestParam String spotifyLink){
        List<SearchResult> searchResultsList = Converter.fromSpotifyToYoutube(spotifyLink);
        model.addAttribute("searchResultList", searchResultsList);
        return "YoutubeListEditor";
    }

    @GetMapping("/mainPage")
    public String getMainPage(Model model, @RequestParam("code") String code){
        YoutubeDataAPI.authorizeWithCode(code);
        return "redirect:/mainPage";
    }

    @GetMapping("/imageToYoutube")
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
        return "searchByPictureYoutube";
    }

    @PostMapping("/remove/{trackIndex}")
    public String removeTrack(@PathVariable("trackIndex") int trackIndex, Model model) throws JsonProcessingException {
        List<SearchResult> trackList = Converter.getLastConvertedYoutubeList();
        trackList.remove(trackIndex);
        model.addAttribute("searchResultList", trackList);
        return "YoutubeListEditor";
    }

    @PostMapping("/upload")
    public String uploadPictures(Model model, @RequestParam("images")List<MultipartFile> multipartFileList) throws IOException, URISyntaxException {
        List<SearchResult> trackList = Converter.fromScreenshotsToYoutube(multipartFileList);
        model.addAttribute("searchResultList", trackList);
        return "YoutubeListEditor";
    }

    @GetMapping("/youtubeListEditor")
    public String getSpotifyListEditor(Model model){
        model.addAttribute("searchResultList", Converter.getLastConvertedYoutubeList());
        return "YoutubeListEditor";
    }

    @PostMapping("/createPlaylist")
    public String createPlaylist(Model model, @RequestParam(value = "isPublic", required = false) String isPublic, @RequestParam String playlistName){
        if(isPublic == null){
            org.example.data.youtube.CreatePlaylist.createPlaylist(playlistName, true,
                    Converter.getLastConvertedYoutubeList());
        }else{
            org.example.data.youtube.CreatePlaylist.createPlaylist(playlistName, false,
                    Converter.getLastConvertedYoutubeList());
        }
        return "PlaylistCreated";
    }

    @GetMapping("/logout")
    public String logout(Model model){
        YoutubeAuthorization.logout();
        return "redirect:/mainPage";
    }
}
