package pl.javalon4.finalproject.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.javalon4.finalproject.dto.*;
import pl.javalon4.finalproject.service.LinkService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/link")
public class LinkController {

    private LinkService linkService;

    @GetMapping("/search")
    public LinkDto getLink(@RequestBody LinkSearcherDto linkSearcherDto) {
        return linkService.search(linkSearcherDto.getDescription());
    }
    @GetMapping
    public List<LinkDto> getAllLinks() {
        return linkService.getAll();
    }
    @GetMapping("/category/{showLinks}")
    public List<LinkCategoryDto> getAllCategories(@PathVariable boolean showLinks) {
        return linkService.getAllCategories(showLinks);
    }
    @PostMapping
    public LinkDto createLink(@RequestBody LinkFormDto linkFormDto, @AuthenticationPrincipal User user) {
        return linkService.createLink(linkFormDto, user.getUsername());
    }
    @PostMapping("/category")
    public LinkCategoryDto createCategory(@RequestBody CategoryFormDto categoryFormDto) {
        return linkService.createCategory(categoryFormDto);
    }
    @PatchMapping
    public LinkDto updateLink(@RequestBody LinkUpdateFormDto linkUpdateFormDto, @AuthenticationPrincipal User user) {
        return linkService.updateLink(linkUpdateFormDto, user.getUsername());
    }
    @PatchMapping("/category")
    public LinkCategoryDto updateCategory(@RequestBody CategoryUpdateFormDto categoryUpdateFormDto ) {
        return linkService.updateCategory(categoryUpdateFormDto.getName());
    }
    @DeleteMapping("/{linkId}")
    public void deleteLink(@PathVariable UUID linkId) {
        linkService.deleteLink(linkId);
    }
    @DeleteMapping("/category/{categoryId}")
    public void deleteCategory(@PathVariable UUID categoryId) {
        linkService.deleteCategory(categoryId);
    }
}