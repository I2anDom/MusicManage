package org.example.service.ImageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.example.data.imageToText.ImageToTextConstants;
import org.example.data.spotify.SearchItem;
import org.example.dto.ImageToTextDTO;
import org.example.dto.ImageToTextNanonetsDTO.Result;
import org.example.dto.ImageToTextNanonetsDTO.Root;
import org.example.dto.ImageToTextNanonetsDTO.Word;
import org.example.dto.ItemToSearchDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageConverter {
//    public static List<ImageToTextDTO> makeRequest(List<MultipartFile> multipartFileList){
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Api-Key", ImageToTextConstants.API_KEY);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("image", multipartFileList.get(0).getResource());
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(ImageToTextConstants.API_URL, requestEntity, String.class);
//        Gson gson = new Gson();
//        Type listType = new TypeToken<List<ImageToTextDTO>>() {}.getType();
//
//        return gson.fromJson(responseEntity.getBody(), listType);
//    }

    public static List<Root> makeRequest(List<MultipartFile> multipartFileList) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("text/plain");
        List<Root> roots = new ArrayList<>();
        for(MultipartFile file : multipartFileList){
            File tempFile = File.createTempFile("prefix", "suffix");
            tempFile.deleteOnExit();
            file.transferTo(tempFile);
            okhttp3.RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file","FILE_NAME",
                            okhttp3.RequestBody.create(MediaType.parse("application/octet-stream"),
                                    tempFile))
                    .build();
            Request request = new Request.Builder()
                    .url("https://app.nanonets.com/api/v2/OCR/FullText")
                    .method("POST", body)
                    .addHeader("Authorization", Credentials.basic("4aa43909-edc2-11ed-ae92-ea08a0755a6a", ""))
                    .build();
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            roots.add(objectMapper.readValue(response.body().string(), Root.class));
        }
        return roots;
    }

    public static List<ItemToSearchDTO> getItemToSearchFromRoots(List<Root> roots) {
        List<ItemToSearchDTO> result = new ArrayList<>();
        for (Root root : roots) {
            List<ItemToSearchDTO> itemToSearchDTOList = new ArrayList<>();
            List<Word> words = root.results.get(0).page_data.get(0).words;
            ItemToSearchDTO testItem;
            int index = trackWasFound(words, 0);
            if(index != 0){
                index = trackWasFound(words, index);
            }
            if(index != 0){
                index = trackWasFound(words, index);
            }
            if(index == 0){
                List<String> wordList = createWordList(words);
                for(int i=0; i<wordList.size(); i+=2){
                    ItemToSearchDTO item = new ItemToSearchDTO();
                    item.setSongName(wordList.get(i));
                    if(wordList.size() > i + 1){
                        item.setArtist(wordList.get(i+1));
                    }
                    itemToSearchDTOList.add(item);
                }
            } else {
                List<String> oneRowSongAndAuthor = createWordList(words);
                for(String row : oneRowSongAndAuthor){
                    if(row.length() > 3){
                        ItemToSearchDTO item = new ItemToSearchDTO();
                        item.setSongName(row);
                        itemToSearchDTOList.add(item);
                    }
                }
            }
            result.addAll(itemToSearchDTOList);
        }
        return result;
    }

    public static int trackWasFound(List<Word> words, int index){
        Word firstWordOfSongName = words.get(index);
        Word firstWordOfAuthorName = null;
        Word firstWordOfSecondName = null;
        String songName = firstWordOfSongName.text;
        for (int i = index + 1; i < words.size(); i++) {
            if (Math.abs(words.get(i).ymin - firstWordOfSongName.ymin) <= 3) {
                songName += " " + words.get(i).text;
            } else {
                firstWordOfAuthorName = words.get(i);
                break;
            }
        }
        String authorName = firstWordOfAuthorName.text;
        for (int i = words.indexOf(firstWordOfAuthorName) + 1; i < words.size(); i++) {
            if (Math.abs(words.get(i).ymin - firstWordOfAuthorName.ymin) <= 3) {
                authorName += " " + words.get(i).text;
            } else{
                firstWordOfSecondName = words.get(i);
                break;
            }
        }
        Track track = SearchItem.searchItem(new ItemToSearchDTO(songName, authorName));
        if(track == null){
            return words.indexOf(firstWordOfSecondName);
        }
        return 0;
    }

    public static List<String> createWordList(List<Word> words) {
        List<String> wordList = new ArrayList<>();

        Word previousWord = null;
        String currentString = null;

        for(Word word : words){
            if(word.text.equals("E")){
            }
            else if(previousWord == null){
                currentString = word.text;
                previousWord = word;
            } else if(Math.abs(previousWord.ymin - word.ymin) >= 5){
                wordList.add(currentString);
                currentString = word.text;
                previousWord = word;
            } else{
                currentString += " " + word.text;
                previousWord = word;
            }
        }
        wordList.add(currentString);
        return wordList;
    }
}
