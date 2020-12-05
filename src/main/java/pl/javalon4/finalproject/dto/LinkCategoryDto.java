package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.javalon4.finalproject.enity.Link;

import java.util.List;

@Data
@AllArgsConstructor
public class LinkCategoryDto {

    private String name;
    private List<Link> links;

    public LinkCategoryDto(String name) {
        this.name = name;
    }
}
