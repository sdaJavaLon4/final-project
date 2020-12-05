package pl.javalon4.finalproject.service;

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

import java.util.UUID;

import static java.util.Collections.emptyList;

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
            this.repository.save(new AppUser(UUID.randomUUID().toString(), userForm.getLogin(), encodedPassword, null,
                    emptyList()));
            return;
        }
        throw new RuntimeException();
    }

    public AppUserDto findByLogin(String username) {

        return mapper.mapToDto(repository.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username)));
    }

    @Transactional
    public AppUserDto update(UserUpdateFormDto updateForm, String username) {
        if (isPasswordFormMatchDatabasePassword(updateForm, username)) {
            final var userEntity = repository.findByLogin(username)
                    .orElseThrow(() -> new UserNotFoundException(username));
            final var userToUpdate = mapper.mapToEntity(updateForm, userEntity);
            repository.save(userToUpdate);
            return mapper.mapToDto(userToUpdate);
        } else
            throw new IncorrectPasswordException();
    }

    @Transactional
    public void delete(String username) {
        repository.delete(repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    private boolean isPasswordFormMatchDatabasePassword(UserUpdateFormDto updateForm, String username) {
        return passwordEncoder.matches(updateForm.getActualPassword(),
                repository.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username)).getPassword());
    }
}
