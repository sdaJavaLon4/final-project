package pl.javalon4.finalproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.javalon4.finalproject.dto.AppUserDto;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.dto.UserUpdateFormDto;
import pl.javalon4.finalproject.service.AppUserService;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {

        this.appUserService = appUserService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserForm userForm) {

        this.appUserService.registerUser(userForm);
    }

    // READ, get info about logged in user
    @GetMapping
    public AppUserDto getUser(@AuthenticationPrincipal User user) {

        return appUserService.findByLogin(user);
        // TODO
    }

    // UPDATE, change password and email
    @PatchMapping
    public AppUserDto updateUser(@RequestBody UserUpdateFormDto updateForm, @AuthenticationPrincipal User user) {
        return appUserService.update(updateForm, user);
        // TODO:
    }

    // DELETE, delete logged in user (and invalidate session - later)
    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal User user) {
        // TODO:
    }
}
