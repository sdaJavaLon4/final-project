package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateFormDto {

    private String password;
    private String email;
    private boolean changePassword;
    private boolean changeEmail;
}
