package org.example.service.auth.youtube.auth;

import autovalue.shaded.com.google.common.collect.Lists;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.rpc.context.AttributeContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.example.data.youtube.YoutubeConstants.*;


public class YoutubeLoginURI {
    public static URI loginURI;
    public static URI getLoginURI(){
        if(loginURI == null){
            List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
            loginURI = authorize(scopes, "playlistupdates");
            return loginURI;
        }else{
            return loginURI;
        }
    }

    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @param scopes              list of scopes needed to run youtube upload.
     * @param credentialDatastore name of the credential datastore to cache OAuth tokens
     */
    public static URI authorize(List<String> scopes, String credentialDatastore)  {

        // Load client secrets.
        Reader clientSecretReader = new InputStreamReader(Objects.requireNonNull(AttributeContext.Auth.class.getResourceAsStream("/client_secrets.json")));
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential "
                            + "into src/main/resources/client_secrets.json");
            System.exit(1);
        }

        if(CLIENT_ID == null){
            CLIENT_ID = clientSecrets.getDetails().getClientId();
        }

        if(CLIENT_SECRET == null){
            CLIENT_SECRET = clientSecrets.getDetails().getClientSecret();
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        FileDataStoreFactory fileDataStoreFactory = null;
        try {
            fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataStore<StoredCredential> datastore = null;
        try {
            datastore = fileDataStoreFactory.getDataStore(credentialDatastore);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(FLOW == null){
            FLOW = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
                    .build();
        }

        REDIRECT_URI = clientSecrets.getDetails().getRedirectUris().get(0);
        String authorizationStr = FLOW
                .newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .build();
        return URI.create(authorizationStr);
    }
}
