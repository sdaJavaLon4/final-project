package pl.javalon4.finalproject.service;

import org.springframework.stereotype.Service;
import pl.javalon4.finalproject.dto.*;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.exception.CategoryNotFoundException;
import pl.javalon4.finalproject.exception.LinkNotFoundException;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.LinkMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;
import pl.javalon4.finalproject.repository.LinkCategoryRepository;
import pl.javalon4.finalproject.repository.LinkRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LinkService {

    final private AppUserRepository appUserRepository;
    final private LinkCategoryRepository categoryRepository;
    final private LinkRepository linkRepository;
    final private LinkMapper mapper;

    public LinkService(AppUserRepository appUserRepository, LinkCategoryRepository categoryRepository,
                       LinkRepository linkRepository, LinkMapper mapper) {
        this.appUserRepository = appUserRepository;
        this.categoryRepository = categoryRepository;
        this.linkRepository = linkRepository;
        this.mapper = mapper;
    }

    public LinkDto search(String description) {
        return mapper.mapToDto(linkRepository.findByDescriptionContainingIgnoreCase(description)
                .orElseThrow(LinkNotFoundException::new));
    }

    public List<LinkDto> getAll() {
        return linkRepository.findAll().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LinkCategoryDto> getAllCategories(boolean showLinks) {
        return categoryRepository.findAll().stream()
                .map(linkCategory -> mapper.mapToDto(linkCategory, showLinks))
                .collect(Collectors.toList());
    }

    public LinkDto createLink(LinkFormDto linkFormDto, String login) {
        AppUser appUser = appUserRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        return mapper.mapToDto(linkRepository.save(mapper.mapToEntity(linkFormDto, appUser)));
    }

    public LinkCategoryDto createCategory(CategoryFormDto categoryFormDto) {
        return mapper.mapToDto(categoryRepository.save(mapper.mapToEntity(categoryFormDto)), false);
    }

    public LinkDto updateLink(LinkUpdateFormDto linkUpdateFormDto, String username) {
        return null;

    }

    public LinkCategoryDto updateCategory(String name) {
        return null;
    }

    public void deleteLink(UUID linkId) {
         linkRepository.delete(linkRepository.findById(linkId).orElseThrow(LinkNotFoundException::new));
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepository.delete(categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new));
    }
}
