package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkFormDto {

    private String url;
    private String description;
    private LinkCategoryDto linkCategory;
}
