package pl.javalon4.finalproject.dto;

import lombok.Data;

@Data
public class UserForm {

    private String login;

    private String password;

    private String repeatedPassword;

    private String email;
}
