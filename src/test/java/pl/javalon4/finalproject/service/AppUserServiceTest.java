package pl.javalon4.finalproject.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.UserMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;

    @Spy
    private UserMapper mapper;

    @Spy
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AppUserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String REPEATED_PASSWORD = "pass";


    @Test
    public void shouldCreateAppUser() {
        //given
        UserForm userForm = new UserForm(LOGIN, PASSWORD, REPEATED_PASSWORD);
        //when
        userService.registerUser(userForm);
        //then
        verify(repository).save(any());
        verifyNoMoreInteractions(repository);
        verify(encoder).encode(PASSWORD);
        verifyNoMoreInteractions(encoder);
    }
    @Test
    public void shouldThrowRunTimeException() {
        //given
        UserForm userForm = new UserForm(LOGIN, PASSWORD, "pass2");
        //then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> userService.registerUser(userForm));
        verifyNoInteractions(repository);
        verifyNoInteractions(encoder);
    }
    @Test
    public void shouldGetUserFromDatabaseByLoginAndMapToAppUserDto() {
        //given
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        AppUser appUser = new AppUser(UUID.randomUUID().toString(),
                LOGIN, PASSWORD, null);
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(appUser));
        //when
        AppUserDto appUserDto = userService.findByLogin(user);
        //then
        assertThat(appUser.getLogin()).isEqualTo(appUserDto.getLogin());
        assertThat(appUser.getEmail()).isEqualTo(appUserDto.getEmail());
        verify(repository).findByLogin(LOGIN);
        verify(mapper).mapToDto(appUser);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(mapper);
    }
    @Test
    public void shouldThrowUserNotFoundExceptionWhenTryGetUserFromDatabase() {
        //given
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.empty());
        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findByLogin(user));
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }
}