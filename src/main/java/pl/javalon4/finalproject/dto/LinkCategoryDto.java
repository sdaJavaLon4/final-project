package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LinkCategoryDto {

    private String name;
    private List<LinkDto> links;

}
