package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorNameSongDTO {
    String author;
    String song;
}
