package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateFormDto {

    private String actualPassword;
    private String newPassword;
    private String email;
}
