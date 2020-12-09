package pl.javalon4.finalproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.javalon4.finalproject.dto.*;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.Link;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.enity.LinkStatus;
import pl.javalon4.finalproject.exception.CategoryNotFoundException;
import pl.javalon4.finalproject.exception.LinkNotFoundException;
import pl.javalon4.finalproject.exception.UserNotFoundException;
import pl.javalon4.finalproject.mapper.LinkMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;
import pl.javalon4.finalproject.repository.LinkCategoryRepository;
import pl.javalon4.finalproject.repository.LinkRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.*;

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

    public Collection<LinkDto> search(String description) {
        return linkRepository.findByDescriptionContainingIgnoreCase(description).stream().map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LinkDto> getAll() {
        return linkRepository.findAll().stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public List<LinkCategoryDto> getAllCategories(boolean showLinks) {
        return categoryRepository.findAll().stream().map(linkCategory -> mapper.mapToDto(linkCategory, showLinks))
                .collect(Collectors.toList());
    }

    public LinkDto createLink(LinkFormDto linkFormDto, String username) {
        AppUser appUser = appUserRepository.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username));
        final var category = categoryRepository.findByName(linkFormDto.getLinkCategory().getName())
                .orElseThrow(CategoryNotFoundException::new);
        return mapper.mapToDto(linkRepository.save(mapper.mapToEntity(linkFormDto, appUser, category)));
    }

    public LinkCategoryDto createCategory(CategoryFormDto categoryFormDto, String username) {
        final var user = appUserRepository.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username));
        return mapper.mapToDto(categoryRepository.save(mapper.mapToEntity(categoryFormDto, user)), false);
    }

    @Transactional
    public LinkDto updateLink(LinkUpdateFormDto linkUpdateFormDto) {
        Link link = linkRepository.findById(linkUpdateFormDto.getId()).orElseThrow(LinkNotFoundException::new);
        if (nonNull(linkUpdateFormDto.getUrl())) {
            link.setUrl(linkUpdateFormDto.getUrl());
        }
        if (nonNull(linkUpdateFormDto.getDescription())) {
            link.setDescription(linkUpdateFormDto.getDescription());
        }
        if (nonNull(linkUpdateFormDto.getCategory())) {
            link.setCategory(categoryRepository.findByName(linkUpdateFormDto.getCategory()
                    .getName()).orElseThrow(CategoryNotFoundException::new));
        }
        if (nonNull(linkUpdateFormDto.getStatus())) {
            link.setStatus(LinkStatus.valueOf(linkUpdateFormDto.getStatus().name()));
        }
        return mapper.mapToDto(linkRepository.save(link));

    }

    public LinkCategoryDto updateCategory(CategoryUpdateFormDto categoryUpdateFormDto) {
        LinkCategory linkCategory = categoryRepository.findByName(categoryUpdateFormDto.getOldName())
                .orElseThrow(CategoryNotFoundException::new);
        linkCategory.setName(categoryUpdateFormDto.getNewName());
        return mapper.mapToDto(categoryRepository.save(linkCategory), false);
    }

    public void deleteLink(String linkId) {
        linkRepository.delete(linkRepository.findById(linkId).orElseThrow(LinkNotFoundException::new));
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepository.delete(categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new));
    }
}
