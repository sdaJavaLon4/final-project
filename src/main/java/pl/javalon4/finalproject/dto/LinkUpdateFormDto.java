package pl.javalon4.finalproject.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LinkUpdateFormDto {

    private String id;
    private String url;
    private String description;
    private LinkStatusDto status;
    private LinkCategoryDto category;

}
