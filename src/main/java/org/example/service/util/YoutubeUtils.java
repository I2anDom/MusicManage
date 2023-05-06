package org.example.service.util;

import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.example.data.youtube.YoutubeDataAPI;
import org.example.dto.AuthorNameSongDTO;

import java.util.ArrayList;
import java.util.List;

public class YoutubeUtils {
    public static List<AuthorNameSongDTO> getAuthorNameSong(PlaylistItemListResponse response){
        List<PlaylistItem> playlistItems = response.getItems();
        List<AuthorNameSongDTO> authorNameSongDTOList = new ArrayList<>();
        for (PlaylistItem item : playlistItems) {
            String videoId = item.getSnippet().getResourceId().getVideoId();

            // Виклик запиту до API для отримання деталей відео
            VideoListResponse videoResponse = null;
            try{
                videoResponse = YoutubeDataAPI.getYoutubeForRequests().videos().list("snippet")
                        .setId(videoId)
                        .execute();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            List<Video> videos = videoResponse.getItems();

            if (videos.size() > 0) {
                String channelTitle = videos.get(0).getSnippet().getChannelTitle();
                String channelTitleWithoutTopic = channelTitle
                        .replace(" - Topic", "")
                        .replace(" (Official Music Video)", "")
                        .replace(" Official Music Video", "")
                        .replace(" [Official Video]", "")
                        .replace(" [Music Video]", "")
                        .replace(" (Official Video)", "")
                        .replace(" Remix", "")
                        .replace(" Lyrics", "");
                String videoName = videos.get(0).getSnippet().getTitle();
                authorNameSongDTOList.add(new AuthorNameSongDTO(channelTitleWithoutTopic, videoName));
            }
        }
        return authorNameSongDTOList;
    }
}
