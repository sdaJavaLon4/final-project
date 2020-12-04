package pl.javalon4.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserUpdateForm {


    private String newEmail;
    //private String oldPassword;
    private String newPassword;



}
