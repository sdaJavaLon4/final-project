package pl.javalon4.finalproject.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.javalon4.finalproject.dto.LinkCategoryDTO;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.service.LinkCategoryService;

@RestController
@RequestMapping("/linkcategory")
public class LinkCategoryController {

    private  final LinkCategoryService linkCategoryService;


    public LinkCategoryController(LinkCategoryService linkCategoryService) {
        this.linkCategoryService = linkCategoryService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewLinkCategory(@RequestBody LinkCategoryDTO category) {

        this.linkCategoryService.addCategory(category);
    }



}
