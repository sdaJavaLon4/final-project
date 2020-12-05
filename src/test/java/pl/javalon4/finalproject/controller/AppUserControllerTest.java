package pl.javalon4.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.mapper.UserMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;
import pl.javalon4.finalproject.service.AppUserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest(properties = {"spring.datasource.username=root", "spring.datasource.password="})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserRepository repository;

    @SpyBean
    private UserMapper mapper;

    @SpyBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    @InjectMocks
    private AppUserService userService;

    @Autowired
    @InjectMocks
    private AppUserController controller;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String REPEATED_PASSWORD = "pass";
    private static final String NEW_PASSWORD = "pass2";
    private static final String EMAIL = "test@test";
    private static final String WRONG_PASSWORD = "pass3";

    @Test
    public void testRegisterUser() throws Exception {
        //given
        final var jsonRequest = "{"
                + "\"login\": \"test\","
                + "\"password\": \"pass\","
                + "\"repeatedPassword\": \"pass\","
                + "}";
        AppUser appUser = new AppUser(UUID.randomUUID().toString(), LOGIN, PASSWORD, EMAIL);
        Mockito.when(repository.save(appUser)).thenReturn(appUser);
        //then
        mockMvc.perform(post("/user")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());


    }
}