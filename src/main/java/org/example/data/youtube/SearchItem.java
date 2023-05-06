package org.example.data.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.example.dto.ItemToSearchDTO;

import java.util.ArrayList;
import java.util.List;

public class SearchItem {
    public static List<SearchResult> searchItems(List<ItemToSearchDTO> itemToSearchDTOList){
        try{
            YouTube youTube = YoutubeDataAPI.getYoutubeForRequests();
            List<SearchResult> searchResultList = new ArrayList<>();
            for(ItemToSearchDTO item : itemToSearchDTOList){
                YouTube.Search.List search = youTube.search().list("id,snippet");
                search.setKey(YoutubeConstants.API_KEY);
                search.setQ(item.getSongName() + " " + item.getArtist());
                search.setType("video");
                search.setMaxResults(1L);
                searchResultList.addAll(search.execute().getItems());
            }
            for(SearchResult result : searchResultList){
                System.out.println(result.getSnippet().getTitle());
            }
            ResourceId resourceId = new ResourceId();
            resourceId.setKind("youtube#video");
            return searchResultList;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}