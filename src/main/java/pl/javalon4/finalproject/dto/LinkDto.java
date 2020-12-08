package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkDto {

    private String id;
    private String url;
    private String description;
    private LinkStatusDto linkStatus;
    private LinkCategoryDto linkCategory;

}
