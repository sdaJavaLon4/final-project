package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AppUserDto {

    private String login;
    private String email;

}
