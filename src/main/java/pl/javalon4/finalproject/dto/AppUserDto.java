package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppUserDto {

    private String login;
    private String email;

    public AppUserDto(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
