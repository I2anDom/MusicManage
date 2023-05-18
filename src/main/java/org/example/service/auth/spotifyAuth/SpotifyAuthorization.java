package org.example.service.auth.spotifyAuth;


import org.apache.hc.core5.http.ParseException;
import org.example.data.spotify.SpotifyConstants;
import org.example.data.spotify.SpotifyDataAPI;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SpotifyAuthorization {
    SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("<02d1a8d41b944da6b3b03720ad733364>")
            .setClientSecret("<30b2a16284144f328205739f4959832c>")
            .setRedirectUri(URI.create("<http://localhost:8080/greeting>"))
            .build();
    private static AuthorizationCodeCredentials AUTHORIZATION_CODE_CREDENTIALS;

    public static void authorize(String code){
        try {
            AuthorizationCodeRequest codeRequest = new AuthorizationCodeRequest.Builder(SpotifyConstants.CLIENT_ID,
                    SpotifyConstants.CLIENT_SECRET)
                    .code(code)
                    .grant_type(SpotifyConstants.GRANT_TYPE)
                    .redirect_uri(SpotifyHttpManager.makeUri(SpotifyConstants.REDIRECT_URL))
                    .build();
            AUTHORIZATION_CODE_CREDENTIALS = codeRequest.execute();
            SpotifyDataAPI.createSpotifyApi(AUTHORIZATION_CODE_CREDENTIALS);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthorizationCodeCredentials getAuthorizationCodeCredentials(){
        return AUTHORIZATION_CODE_CREDENTIALS;
    }

    public static void logout(){
        AUTHORIZATION_CODE_CREDENTIALS = null;
        SpotifyDataAPI.logout();
    }
}
