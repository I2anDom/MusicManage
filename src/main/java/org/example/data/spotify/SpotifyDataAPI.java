package org.example.data.spotify;

import org.example.dto.ItemToSearchDTO;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

import java.util.ArrayList;
import java.util.List;

public class SpotifyDataAPI {
    private static SpotifyApi SPOTIFY_API;
    public static void createSpotifyApi(AuthorizationCodeCredentials authorizationCodeCredentials) {
        // Встановлення параметрів авторизації
        String clientId = "Ваш_Client_ID"; // Ваш Client ID
        String clientSecret = "Ваш_Client_Secret"; // Ваш Client Secret
        String redirectUri = "http://localhost:8888/callback"; // URL-адреса, на яку Spotify перенаправить після авторизації користувача
        String accessToken = "Ваш_access_token"; // Опціонально: можна передати access token, якщо він вже є
        String refreshToken = "Ваш_refresh_token"; // Опціонально: можна передати refresh token, якщо він вже є

        // Створення об'єкту SpotifyApi з використанням вказаних параметрів
        SPOTIFY_API = new SpotifyApi.Builder()
                .setClientId(SpotifyConstants.CLIENT_ID)
                .setClientSecret(SpotifyConstants.CLIENT_SECRET)
                .setRedirectUri(SpotifyHttpManager.makeUri(SpotifyConstants.REDIRECT_URL))
                .setAccessToken(authorizationCodeCredentials.getAccessToken())
                .setRefreshToken(authorizationCodeCredentials.getRefreshToken())
                .build();
    }

    public static SpotifyApi getSpotifyApi(){
        return SPOTIFY_API;
    }

    public static SpotifyApi getUnauthSpotifyApi(){
        return new SpotifyApi.Builder()
                .setClientId(SpotifyConstants.CLIENT_ID)
                .setClientSecret(SpotifyConstants.CLIENT_SECRET)
                .setRedirectUri(SpotifyHttpManager.makeUri(SpotifyConstants.REDIRECT_URL))
                .build();
    }

    public static List<ItemToSearchDTO> getItemToSearchDTOFromLink(String link){
        try{
            String playlistId = link.split("playlist/")[1];
            GetPlaylistRequest getPlaylistRequest = SPOTIFY_API.getPlaylist(playlistId).build();
            Playlist playlist = getPlaylistRequest.execute();
            Paging<PlaylistTrack> tracks = playlist.getTracks();

            List<ItemToSearchDTO> itemsFromSpotify = new ArrayList<>();
            PlaylistTrack[] playlistTracks = tracks.getItems();

            for(int i=0; i<playlistTracks.length; i++){
                Track track = (Track) playlistTracks[i].getTrack();
                itemsFromSpotify.add(new ItemToSearchDTO(track.getName(), track.getArtists()[0].getName()));
            }
            return itemsFromSpotify;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
