package org.example.data.youtube;

import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.apache.hc.core5.function.Callback;
import org.springframework.boot.util.LambdaSafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePlaylist {
    public static String createPlaylist(String title, boolean isPublic, List<SearchResult> searchResultList) {
        YouTube youtube = YoutubeDataAPI.getYoutubeForRequests();
        String playlistId = createEmptyPlaylist(youtube, title, isPublic);
//        addVideosToPlaylist(youtube, searchResultList.stream().map(SearchResult::getId).map(ResourceId::getVideoId).collect(Collectors.toList()), playlistId);
        for (SearchResult searchResult : searchResultList) {
            ResourceId resourceId = new ResourceId();
            resourceId.setKind("youtube#video");
            resourceId.setVideoId(searchResult.getId().getVideoId());
            addVideoToPlaylist(youtube, searchResult.getId().getVideoId(), playlistId);
        }

        return null;
    }

    private static String createEmptyPlaylist(YouTube youtube, String title, boolean isPublic) {
        try {
            Playlist playlist = new Playlist();
            PlaylistSnippet playlistSnippet = new PlaylistSnippet();
            playlistSnippet.setTitle(title);
            playlist.setSnippet(playlistSnippet);
            PlaylistStatus playlistStatus = new PlaylistStatus();
            playlistStatus.setPrivacyStatus(isPublic ? "public" : "private");
            playlist.setStatus(playlistStatus);

            YouTube.Playlists.Insert insert = youtube.playlists().insert("snippet,status", playlist);
            Playlist newPlaylist = insert.execute();
            return newPlaylist.getId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void addVideoToPlaylist(YouTube youtube, String videoId, String playlistId) {
        try {
            // Create a new playlist item with the video id and playlist id.
            PlaylistItem playlistItem = new PlaylistItem();
            PlaylistItemSnippet snippet = new PlaylistItemSnippet();
            snippet.setPlaylistId(playlistId);
            ResourceId resourceId = new ResourceId();
            resourceId.setKind("youtube#video");
            resourceId.setVideoId(videoId);
            snippet.setResourceId(resourceId);
            playlistItem.setSnippet(snippet);

            // Call the API to add the video to the playlist.
            youtube.playlistItems()
                    .insert("snippet", playlistItem)
                    .execute();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void addVideosToPlaylist(YouTube youtube, List<String> videoIds, String playlistId) {
        try {
            BatchRequest batch = youtube.batch();

            for (String videoId : videoIds) {
                PlaylistItem playlistItem = new PlaylistItem();
                PlaylistItemSnippet snippet = new PlaylistItemSnippet();
                snippet.setPlaylistId(playlistId);
                ResourceId resourceId = new ResourceId();
                resourceId.setKind("youtube#video");
                resourceId.setVideoId(videoId);
                snippet.setResourceId(resourceId);
                playlistItem.setSnippet(snippet);

                youtube.playlistItems().insert("snippet", playlistItem).queue(batch, new PlaylistItemInsertCallback(videoId) {
                });
            }

            batch.execute();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static class PlaylistItemInsertCallback extends JsonBatchCallback<PlaylistItem> {
        private final String videoId;

        public PlaylistItemInsertCallback(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public void onSuccess(PlaylistItem playlistItem, HttpHeaders httpHeaders) throws IOException {
            System.out.println("Video added to playlist: " + videoId);
        }

        @Override
        public void onFailure(GoogleJsonError googleJsonError, HttpHeaders httpHeaders) throws IOException {
            System.out.println("Couldn't add video to playlist" + videoId);
            System.out.println(googleJsonError.getMessage());
            System.out.println(googleJsonError.getErrors());
        }
    }
}
