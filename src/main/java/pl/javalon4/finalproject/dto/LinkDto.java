package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.enity.LinkStatus;

@Data
@AllArgsConstructor
public class LinkDto {

    private String url;
    private String description;
    private LinkStatusDto linkStatus;
    private LinkCategoryDto linkCategory;

}
