package org.example.data.spotify;

import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

public class SpotifyConstants {
    public static final String CLIENT_ID = "02d1a8d41b944da6b3b03720ad733364";
    public static final String CLIENT_SECRET = "30b2a16284144f328205739f4959832c";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String REDIRECT_URL = "http://localhost:8080/greeting";

    public static final URI REDIRECT_URI = SpotifyHttpManager.makeUri(REDIRECT_URL);

}
