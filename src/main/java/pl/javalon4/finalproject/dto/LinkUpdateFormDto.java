package pl.javalon4.finalproject.dto;

import lombok.Data;

@Data
public class LinkUpdateFormDto {

    private String url;
    private String description;
    private LinkStatusDto status;
    private LinkCategoryDto category;

}
