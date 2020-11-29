package pl.javalon4.finalproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldRestController {

    @GetMapping
    public String test() {
        return "Hello SDA final project!";
    }
}
