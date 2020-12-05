package pl.javalon4.finalproject.dto;

import lombok.Data;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.enity.LinkStatus;

@Data
public class LinkUpdateFormDto {

    private String url;
    private String description;
    private LinkStatus status;
    private LinkCategory category;

}
