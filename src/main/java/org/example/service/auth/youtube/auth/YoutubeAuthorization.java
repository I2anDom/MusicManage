package org.example.service.auth.youtube.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.rpc.context.AttributeContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

import static org.example.data.youtube.YoutubeConstants.*;

public class YoutubeAuthorization {

    public static GoogleCredential getCredential(String authorizationCode){
        GoogleTokenResponse response = null;
        try {
            response = FLOW.newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .build()
                .setAccessToken(response.getAccessToken())
                .setRefreshToken(response.getRefreshToken());
    }
}
