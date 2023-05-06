package org.example.data.spotify;

import org.apache.hc.core5.http.ParseException;
import org.example.dto.ItemToSearchDTO;
import org.example.service.auth.spotifyAuth.SpotifyAuthorization;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SearchResult;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchItem {
    public static List<Track> searchItems(List<ItemToSearchDTO> items){
        try{
            AuthorizationCodeCredentials authorizationCodeCredentials = SpotifyAuthorization.getAuthorizationCodeCredentials();
            SpotifyApi spotifyApi = SpotifyDataAPI.getSpotifyApi();
            List<Track> trackList = new ArrayList<>();
            for(ItemToSearchDTO item : items){
                SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(item.getSongName())
                        .limit(30)
                        .build();
                Paging<Track> trackPaging = searchTracksRequest.execute();
                Track[] tracks = trackPaging.getItems();
                SearchArtistsRequest searchArtistRequest = spotifyApi.searchArtists(item.getArtist())
                        .limit(30)
                        .build();
                Paging<Artist> artistPaging = searchArtistRequest.execute();
                Artist[] artists = artistPaging.getItems();
                Track foundTrack = null;
                for(Track track : tracks){
                    ArtistSimplified[] artistSimplifieds = track.getArtists();
                    for(Artist artist : artists){
                        for(ArtistSimplified artistSimplified : artistSimplifieds){
                            if(artist.getId().equals(artistSimplified.getId())){
                                foundTrack = track;
                                break;
                            }
                        }
                    }
                }

                if(foundTrack != null){
                    trackList.add(foundTrack);
                } else{
                    SearchTracksRequest searchTracksByNameAndAuthorRequest = spotifyApi.searchTracks(item.getSongName() + "artist:" + item.getArtist())
                            .limit(1)
                            .build();
                    Track[] tracksBySongNameAndAuthor = searchTracksByNameAndAuthorRequest.execute().getItems();
                    if(tracksBySongNameAndAuthor != null && tracksBySongNameAndAuthor.length > 0){
                        trackList.add(tracksBySongNameAndAuthor[0]);
                    }
                }
            }
            return trackList;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        return null;
    }

}
