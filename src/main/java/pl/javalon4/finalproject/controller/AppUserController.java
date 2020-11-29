package pl.javalon4.finalproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.service.AppUserService;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserForm userForm) {
        this.appUserService.registerUser(userForm);
    }
}
