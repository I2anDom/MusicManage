package org.example.controller;

import org.example.service.converter.Converter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spotify")
public class SpotifyAuthController {
    @GetMapping("/auth")
    public void spotifyLogin(){
    }

    @PostMapping("/convert")
    public String convert(Model model, @RequestParam String youtubeLink){
        Converter.fromYoutubeToSpotify(youtubeLink);
        return "mainPage";
    }
}
