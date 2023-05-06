package org.example.controller;

import org.example.service.auth.spotifyAuth.SpotifyLoginURI;
import org.example.service.auth.spotifyAuth.SpotifyAuthorization;
import org.example.service.auth.youtube.auth.YoutubeLoginURI;
import org.example.service.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@RequestMapping("/")
public class MainPageController {

    @GetMapping("/greeting")
    public String greeting(Model model, @RequestParam("code") String code) {
        SpotifyAuthorization.authorize(code);
        return "searchPage";
    }

    @GetMapping("/mainPage")
    public String getMainPage(Model model){
        URI spotifyURI = SpotifyLoginURI.authorizationCodeUri_Sync();
        URI youtubeURI = YoutubeLoginURI.getLoginURI();
        model.addAttribute("spotifyURI", spotifyURI.toString());
        model.addAttribute("youtubeURI", youtubeURI.toString());
        return "mainPage";
    }

    @GetMapping("/spotifySearcher")
    public String getSearchPage(Model model, @RequestParam("code") String code){
        return "searchPage";
    }
}
