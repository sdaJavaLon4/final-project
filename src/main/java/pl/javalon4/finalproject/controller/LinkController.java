package pl.javalon4.finalproject.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.javalon4.finalproject.dto.*;
import pl.javalon4.finalproject.service.LinkService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/search")
    public Collection<LinkDto> getLink(@RequestBody LinkSearcherDto linkSearcherDto,
            @AuthenticationPrincipal User user) {
        return linkService.search(linkSearcherDto.getDescription(), user.getUsername());
    }

    @GetMapping
    public List<LinkDto> getAllLinks(@AuthenticationPrincipal User user) {
        return linkService.getAll(user.getUsername());
    }

    @GetMapping("/pageable")
    public Page<LinkDto> getAllLinksPageable(@AuthenticationPrincipal User user,
            @RequestParam(name = "count") int countPerPage, @RequestParam(name = "page") int pageNumber) {
        return linkService
                .getAllPageable(user.getUsername(), PageRequest.of(pageNumber, countPerPage, Sort.by("description")));
    }

    @GetMapping("/category/{showLinks}")
    public List<LinkCategoryDto> getAllCategories(@PathVariable boolean showLinks, @AuthenticationPrincipal User user) {
        return linkService.getAllCategories(showLinks, user.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LinkDto createLink(@RequestBody LinkFormDto linkFormDto, @AuthenticationPrincipal User user) {
        return linkService.createLink(linkFormDto, user.getUsername());
    }

    @PostMapping("/category")
    public LinkCategoryDto createCategory(@RequestBody CategoryFormDto categoryFormDto,
            @AuthenticationPrincipal User user) {
        return linkService.createCategory(categoryFormDto, user.getUsername());
    }

    @PatchMapping
    public LinkDto updateLink(@RequestBody LinkUpdateFormDto linkUpdateFormDto, @AuthenticationPrincipal User user) {
        return linkService.updateLink(linkUpdateFormDto, user.getUsername());
    }

    @PatchMapping("/category")
    public LinkCategoryDto updateCategory(@RequestBody CategoryUpdateFormDto categoryUpdateFormDto,
            @AuthenticationPrincipal User user) {
        return linkService.updateCategory(categoryUpdateFormDto, user.getUsername());
    }

    @DeleteMapping("/{linkId}")
    public void deleteLink(@PathVariable String linkId, @AuthenticationPrincipal User user) {
        linkService.deleteLink(linkId, user.getUsername());
    }

    @DeleteMapping("/category/{categoryId}")
    public void deleteCategory(@PathVariable String categoryId, @AuthenticationPrincipal User user) {
        linkService.deleteCategory(categoryId, user.getUsername());
    }
}
