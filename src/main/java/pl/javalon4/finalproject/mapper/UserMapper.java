package pl.javalon4.finalproject.mapper;

import org.springframework.stereotype.Component;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.enity.AppUser;

@Component
public class UserMapper {

    public AppUserDto mapToDto(AppUser appUser) {
        return new AppUserDto(appUser.getLogin(), appUser.getEmail());
    }
}
