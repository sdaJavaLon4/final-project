package pl.javalon4.finalproject.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.dto.UserUpdateFormDto;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.exception.IncorrectPasswordException;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.UserMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;


@SpringBootTest(properties = {"spring.datasource.username=root", "spring.datasource.password="})
@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @MockBean
    private AppUserRepository repository;

    @SpyBean
    private UserMapper mapper;

    @SpyBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    @InjectMocks
    private AppUserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String REPEATED_PASSWORD = "pass";
    private static final String NEW_PASSWORD = "pass2";
    private static final String EMAIL = "test@test";
    private static final String WRONG_PASSWORD = "pass3";


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
        UserForm userForm = new UserForm(LOGIN, PASSWORD, WRONG_PASSWORD);
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
    @Test
    public void test() {
        String encode = encoder.encode(PASSWORD);
        String encode2 = encoder.encode(PASSWORD);
        assertThat(encode2).isEqualTo(encode);
    }
    @Test
    public void shouldUpdateUserEmailAndPassword() {
        //given
        String id = UUID.randomUUID().toString();
        UserUpdateFormDto updateFormDto = new UserUpdateFormDto(PASSWORD, NEW_PASSWORD, EMAIL,
                true, true);
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        AppUser appUser = new AppUser(id,
                LOGIN, encoder.encode(PASSWORD), null);
        AppUser updatedAppUser = new AppUser(id, LOGIN, NEW_PASSWORD, EMAIL);
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(appUser), Optional.of(updatedAppUser));
        when(repository.updatePasswordAndEmail(LOGIN, updateFormDto.getNewPassword(), updateFormDto.getEmail())).thenReturn(2);
        //when
        AppUserDto appUserDto = userService.update(updateFormDto, user);
        //then
        assertThat(appUserDto.getLogin()).isEqualTo(updatedAppUser.getLogin());
        assertThat(appUserDto.getEmail()).isEqualTo(updatedAppUser.getEmail());
        verify(repository, times(2)).findByLogin(LOGIN);
        //verify(repository).updatePasswordAndEmail(LOGIN, updateFormDto.getNewPassword(), updateFormDto.getEmail());
        verify(mapper).mapToDto(updatedAppUser);

    }
    @Test
    public void shouldUpdateUserPassword() {
        //given
        String id = UUID.randomUUID().toString();
        UserUpdateFormDto updateFormDto = new UserUpdateFormDto(PASSWORD, NEW_PASSWORD, EMAIL,
                true, false);
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        AppUser appUser = new AppUser(id,
                LOGIN, encoder.encode(PASSWORD), null);
        AppUser updatedAppUser = new AppUser(id, LOGIN, NEW_PASSWORD, EMAIL);
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(appUser), Optional.of(updatedAppUser));
        when(repository.updateEmail(LOGIN, updateFormDto.getEmail())).thenReturn(1);
        //when
        AppUserDto appUserDto = userService.update(updateFormDto, user);
        //then
        assertThat(appUserDto.getLogin()).isEqualTo(updatedAppUser.getLogin());
        assertThat(appUserDto.getEmail()).isEqualTo(updatedAppUser.getEmail());
        verify(repository, times(2)).findByLogin(LOGIN);
        //verify(repository).updatePasswordAndEmail(LOGIN, updateFormDto.getNewPassword(), updateFormDto.getEmail());
        verify(mapper).mapToDto(updatedAppUser);

    }
    @Test
    public void shouldUpdateUserEmail() {
        //given
        String id = UUID.randomUUID().toString();
        UserUpdateFormDto updateFormDto = new UserUpdateFormDto(PASSWORD, NEW_PASSWORD, EMAIL,
                false, true);
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        AppUser appUser = new AppUser(id,
                LOGIN, encoder.encode(PASSWORD), null);
        AppUser updatedAppUser = new AppUser(id, LOGIN, NEW_PASSWORD, EMAIL);
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(appUser), Optional.of(updatedAppUser));
        when(repository.updateEmail(LOGIN, updateFormDto.getEmail())).thenReturn(1);
        //when
        AppUserDto appUserDto = userService.update(updateFormDto, user);
        //then
        assertThat(appUserDto.getLogin()).isEqualTo(updatedAppUser.getLogin());
        assertThat(appUserDto.getEmail()).isEqualTo(updatedAppUser.getEmail());
        verify(repository, times(2)).findByLogin(LOGIN);
        //verify(repository).updatePasswordAndEmail(LOGIN, updateFormDto.getNewPassword(), updateFormDto.getEmail());
        verify(mapper).mapToDto(updatedAppUser);

    }
    @Test
    public void shouldThrowIncorrectPasswordException() {
        //given
        String id = UUID.randomUUID().toString();
        UserUpdateFormDto updateFormDto = new UserUpdateFormDto(WRONG_PASSWORD, NEW_PASSWORD, EMAIL,
                true, true);
        User user = new User(LOGIN, PASSWORD, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        AppUser appUser = new AppUser(id,
                LOGIN, encoder.encode(PASSWORD), null);
        when(repository.findByLogin(LOGIN)).thenReturn(Optional.of(appUser));
        //then
        assertThatExceptionOfType(IncorrectPasswordException.class)
                .isThrownBy(() -> userService.update(updateFormDto, user));

    }
}