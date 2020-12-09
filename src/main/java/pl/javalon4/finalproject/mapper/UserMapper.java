package pl.javalon4.finalproject.mapper;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserUpdateFormDto;
import pl.javalon4.finalproject.enity.AppUser;

import java.util.UUID;

@Component
public class UserMapper {

    public AppUserDto mapToDto(AppUser appUser) {

        return new AppUserDto(appUser.getLogin(), appUser.getEmail());
    }

    public AppUser mapToEntity(UserUpdateFormDto updateFormDto, AppUser user) {
        user.setEmail(updateFormDto.getEmail());
        user.setPassword(updateFormDto.getNewPassword());
        return user;
    }
}
