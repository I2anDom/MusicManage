package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okio.ByteString;
import org.example.data.imageToText.ImageToTextConstants;
import org.example.dto.CategoryDTO;
import org.example.dto.ImageToTextDTO;

import org.example.dto.ImageToTextNanonetsDTO.Root;
import org.example.dto.NanonetsRequestDTO;
import org.example.service.converter.Converter;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/imageToSpotify")
    public String convert(Model model){
        return "searchByPicture";
    }

    @PostMapping("/upload")
    public String uploadPictures(Model model, @RequestParam("images")List<MultipartFile> multipartFileList) throws IOException, URISyntaxException {
        Converter.fromScreenshotsToPlaylist(multipartFileList);

        return null;
    }

}
