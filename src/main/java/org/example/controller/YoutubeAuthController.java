package org.example.controller;

import org.example.data.youtube.YoutubeDataAPI;
import org.example.service.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/youtube")
public class YoutubeAuthController {
    @GetMapping("/mainPage")
    public String getMainPage(Model model, @RequestParam("code") String code){
        YoutubeDataAPI.authorizeWithCode(code);
        return "mainPage";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "mainPage";
    }

    @PostMapping("/convert")
    public String convert(Model model, @RequestParam String spotifyLink){
        Converter.fromSpotifyToYoutube(spotifyLink);
        return "mainPage";
    }
}
