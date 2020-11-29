package pl.javalon4.finalproject.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.repository.AppUserRepository;

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
            this.repository.save(new AppUser(UUID.randomUUID().toString(), userForm.getLogin(), encodedPassword, null));
            return;
        }
        throw new RuntimeException();
    }

    public AppUser findByLogin(String login) {

        return repository.findByLogin(login);
    }
}
