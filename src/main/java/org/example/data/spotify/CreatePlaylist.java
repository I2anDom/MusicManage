package org.example.data.spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePlaylist {
    public static void createPlaylist(String playlistName, boolean isPublic, List<Track> trackList){
        try{
            SpotifyApi spotifyApi = SpotifyDataAPI.getSpotifyApi();
            GetCurrentUsersProfileRequest currentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
            User user = currentUsersProfileRequest.execute();
            String userId = user.getId();
            CreatePlaylistRequest createPlaylistRequest = spotifyApi
                    .createPlaylist(userId, playlistName)
                    .public_(true)
                    .collaborative(false)
                    .description("myPlaylist")
                    .build();
            Playlist playlist = createPlaylistRequest.execute();
            AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi
                    .addItemsToPlaylist(playlist.getId(), trackList.stream()
                            .map(Track::getUri)
                            .toArray(String[]::new))
                    .build();
            SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
