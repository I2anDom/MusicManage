package org.example.data.youtube;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class YoutubeConstants {
    public static String  API_KEY = "AIzaSyBPLjX-Ml8wMB3HgKIQWR3gUDiWJNTRIP0";
    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    public static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

    public static GoogleAuthorizationCodeFlow FLOW;

    public static String REDIRECT_URI;

    public static String CLIENT_ID;

    public static String CLIENT_SECRET;
}
