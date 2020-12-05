package pl.javalon4.finalproject.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.Collections;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return appUserRepository.findByLogin(username)
                .stream()
                .map(appUser -> new User(appUser.getLogin(), appUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

    }
}
