package pl.javalon4.finalproject.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.dto.UserUpdateFormDto;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.exception.IncorrectPasswordException;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.UserMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.Optional;
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

        return mapper.mapToDto(repository.findByLogin(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername())));
    }

    @Transactional
    public AppUserDto update(UserUpdateFormDto updateForm, User user) {
        if (isPasswordFormMatchDatabasePassword(updateForm, user)) {
            repository.save(mapper.mapToEntity(updateForm, user));
            return mapper.mapToDto(repository.findByLogin(user.getUsername())
                    .orElseThrow(() -> new UserNotFoundException(user.getUsername())));
        } else throw new IncorrectPasswordException();
    }

    private boolean isPasswordFormMatchDatabasePassword(UserUpdateFormDto updateForm, User user) {
        return passwordEncoder.matches(updateForm.getActualPassword(), repository.findByLogin(user.getUsername()).get().getPassword());
    }
    @Transactional
    public void delete(User user) {
        repository.delete(repository.findByLogin(user.getUsername())
                .orElseThrow(()-> new UsernameNotFoundException(user.getUsername())));
    }
}
