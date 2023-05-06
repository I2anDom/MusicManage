package org.example.service.auth.spotifyAuth;

import org.example.data.spotify.SpotifyConstants;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

public class SpotifyLoginURI {
    private static final String clientId = "02d1a8d41b944da6b3b03720ad733364";
    private static final String clientSecret = "30b2a16284144f328205739f4959832c";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(SpotifyConstants.CLIENT_ID)
            .setClientSecret(SpotifyConstants.CLIENT_SECRET)
            .setRedirectUri(SpotifyConstants.REDIRECT_URI)
            .build();
    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
          .scope("playlist-modify-public,playlist-modify-private,user-library-modify")
          .show_dialog(true)
            .build();

    public static URI authorizationCodeUri_Sync() {
        return authorizationCodeUriRequest.execute();
    }
}
