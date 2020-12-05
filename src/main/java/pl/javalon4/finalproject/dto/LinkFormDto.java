package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.javalon4.finalproject.enity.LinkCategory;

@Data
@AllArgsConstructor
public class LinkFormDto {

    private String url;
    private String description;
    private LinkCategory linkCategory;
}
