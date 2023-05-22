package org.example.controller;

import org.apache.hc.core5.http.ParseException;
import org.example.data.spotify.SpotifyDataAPI;
import org.example.data.youtube.YoutubeDataAPI;
import org.example.service.auth.spotifyAuth.SpotifyLoginURI;
import org.example.service.auth.spotifyAuth.SpotifyAuthorization;
import org.example.service.auth.youtube.auth.YoutubeAuthorization;
import org.example.service.auth.youtube.auth.YoutubeLoginURI;
import org.example.service.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.io.IOException;
import java.net.URI;

@Controller
@RequestMapping("/")
public class MainPageController {

    @GetMapping("/greeting")
    public String greeting(Model model, @RequestParam("code") String code) {
        SpotifyAuthorization.authorize(code);
        return "redirect:/mainPage";
    }

    @GetMapping("/mainPage")
    public String getMainPage(Model model) throws IOException, ParseException, SpotifyWebApiException {
        URI spotifyURI = SpotifyLoginURI.authorizationCodeUri_Sync();
        URI youtubeURI = YoutubeLoginURI.getLoginURI();
        model.addAttribute("spotifyURI", spotifyURI.toString());
        model.addAttribute("youtubeURI", youtubeURI.toString());
        model.addAttribute("spotifyUsername", SpotifyDataAPI.getDisplayNameOfCurrentAuthorizedUser());
        model.addAttribute("youtubeChannel", YoutubeDataAPI.getChanelNameOfCurrentAuthorizedUser());
        return "SelectWhatToConvert";
    }

    @GetMapping("/spotifySearcher")
    public String getSearchPage(Model model, @RequestParam("code") String code){
        return "searchPage";
    }

    @GetMapping("/test")
    public String testPage(){
        System.out.println("Success");
        return "mainPage";
    }
}
