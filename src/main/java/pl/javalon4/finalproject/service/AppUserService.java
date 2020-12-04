package pl.javalon4.finalproject.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.dto.UserUpdateForm;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserForm userForm) {
        if (userForm.getPassword().equals(userForm.getRepeatedPassword())) {
            final var encodedPassword = passwordEncoder.encode(userForm.getPassword());
            this.repository.save(new AppUser(UUID.randomUUID().toString(), userForm.getLogin(), encodedPassword, userForm.getEmail()));
            return;
        }
        throw new RuntimeException();
    }

    public AppUserDto findByLogin(User user) {

        AppUser byLogin = repository.findByLogin(user.getUsername());
        return new AppUserDto(user.getUsername(), byLogin.getEmail());

    }

    public AppUserDto updateUserByLogin(UserUpdateForm updateForm, User user) {
        String encodedPassword = passwordEncoder.encode(updateForm.getNewPassword());
        AppUser byLogin = repository.findByLogin(user.getUsername());
        byLogin.setPassword(encodedPassword);
        byLogin.setEmail(updateForm.getNewEmail());
        repository.save(byLogin);
        return new AppUserDto(byLogin.getLogin(), byLogin.getEmail());
    }

    public void deleteUserByLogin(User user) {
        AppUser bylogin = repository.findByLogin(user.getUsername());
        repository.delete(bylogin);
    }

//    public List<AppUserDto> getAllUsers() {
//        repository.findAll();
//        return ;
//    }
}
