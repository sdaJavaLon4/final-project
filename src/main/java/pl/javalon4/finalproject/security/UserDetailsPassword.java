package pl.javalon4.finalproject.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;

public class UserDetailsPassword implements UserDetailsPasswordService {
    @Override
    public UserDetails updatePassword(UserDetails userDetails, String s) {


        return null;
    }
}
