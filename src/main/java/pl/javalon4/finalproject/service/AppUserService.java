package pl.javalon4.finalproject.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
        return mapper.mapToDto(repository.findByLogin(user.getUsername()));
    }


    public AppUserDto update(UserUpdateFormDto updateForm, User user) {
        if (updateForm.isChangePassword() && updateForm.isChangePassword()) {
            try {
                changePasswordAndEmail(user.getUsername(), updateForm.getPassword(), updateForm.getEmail());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        } else if (updateForm.isChangePassword()) {
            try {
                changePassword(user.getUsername(), updateForm.getPassword());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                changeEmail(user.getUsername(), updateForm.getEmail());
            } catch (UserNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mapper.mapToDto(repository.findByLogin(user.getUsername()));
    }

    private void changePasswordAndEmail(String login, String password, String email) throws UserNotFoundException {
        if (!repository.updatePasswordAndEmail(login, passwordEncoder.encode(password), email)) {
            throw new UserNotFoundException(String.format("User %s not found", login));
        }

    }

    private void changePassword(String login, String password) throws UserNotFoundException {
        if (!repository.updatePassword(login, passwordEncoder.encode(password))) {
            throw new UserNotFoundException(String.format("User %s not found", login));
        }
    }

    private void changeEmail(String login, String email) throws UserNotFoundException {
        if (!repository.updateEmail(login, email)) {
            throw new UserNotFoundException(String.format("User %s not found", login));
        }
    }
}
