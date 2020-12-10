package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryUpdateFormDto {

    private String oldName;
    private String newName;
}
