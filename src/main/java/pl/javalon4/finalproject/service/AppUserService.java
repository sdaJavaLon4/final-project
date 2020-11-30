package pl.javalon4.finalproject.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.dto.UserUpdateFormDto;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.UserMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.UUID;

@Service
public class AppUserService {

    private final AppUserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository, UserMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserForm userForm) {
        if (userForm.getPassword().equals(userForm.getRepeatedPassword())) {
            final var encodedPassword = passwordEncoder.encode(userForm.getPassword());
            this.repository.save(new AppUser(UUID.randomUUID().toString(), userForm.getLogin(), encodedPassword, null));
            return;
        }
        throw new RuntimeException();
    }

    public AppUserDto findByLogin(User user) {

        //AppUser byLogin = repository.findByLogin(user.getUsername());
        //return new AppUserDto(user.getUsername(), byLogin.getEmail());
        return mapper.mapToDto(repository.findByLogin(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername())));
    }

    @Transactional
    public AppUserDto update(UserUpdateFormDto updateForm, User user) {

        if (updateForm.isChangePassword() && updateForm.isChangeEmail()) {
            changePasswordAndEmail(user.getUsername(), updateForm.getNewPassword(), updateForm.getEmail());
        } else if (updateForm.isChangePassword()) {
            changePassword(user.getUsername(), updateForm.getNewPassword());
        } else {
            changeEmail(user.getUsername(), updateForm.getEmail());
        }
        return mapper.mapToDto(repository.findByLogin(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername())));
    }

    private void changePasswordAndEmail(String login, String password, String email) {
        repository.updatePasswordAndEmail(login, passwordEncoder.encode(password), email);

    }

    private void changePassword(String login, String password) {
        repository.updatePassword(login, passwordEncoder.encode(password));
    }

    private void changeEmail(String login, String email) {
        repository.updateEmail(login, email);
    }
}
