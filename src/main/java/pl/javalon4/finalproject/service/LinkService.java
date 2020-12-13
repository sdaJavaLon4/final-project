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
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

// TODO: move category related part to LinkCategoryService
@Service
public class LinkService {

    final private AppUserRepository appUserRepository;

    // TODO: after refactoring it should be change to LinkCategoryService dependency
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

    public Collection<LinkDto> search(String description, String username) {
        final AppUser user = findAppUser(username);
        return linkRepository.findByDescriptionContainingIgnoreCaseAndUser(description, user).stream()
                .map(mapper::mapToDto).collect(Collectors.toList());
    }

    private AppUser findAppUser(String username) {
        return appUserRepository.findByLogin(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public List<LinkDto> getAll(String username) {
        return linkRepository.findByUser(findAppUser(username)).stream().map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LinkCategoryDto> getAllCategories(boolean showLinks, String username) {
        return categoryRepository.findByUser(findAppUser(username)).stream()
                .map(linkCategory -> mapper.mapToDto(linkCategory, showLinks)).collect(Collectors.toList());
    }

    public LinkDto createLink(LinkFormDto linkFormDto, String username) {
        AppUser appUser = findAppUser(username);
        final var category = categoryRepository.findByNameAndUser(linkFormDto.getLinkCategory().getName(), appUser)
                .orElseThrow(CategoryNotFoundException::new);
        return mapper.mapToDto(linkRepository.save(mapper.mapToEntity(linkFormDto, appUser, category)));
    }

    public LinkCategoryDto createCategory(CategoryFormDto categoryFormDto, String username) {
        final AppUser user = findAppUser(username);
        return mapper.mapToDto(categoryRepository.save(mapper.mapToEntity(categoryFormDto, user)), false);
    }

    @Transactional
    public LinkDto updateLink(LinkUpdateFormDto linkUpdateFormDto, String username) {
        final var user = findAppUser(username);
        Link link = linkRepository.findByIdAndUser(linkUpdateFormDto.getId(), user)
                .orElseThrow(LinkNotFoundException::new);
        if (nonNull(linkUpdateFormDto.getUrl())) {
            link.setUrl(linkUpdateFormDto.getUrl());
        }
        if (nonNull(linkUpdateFormDto.getDescription())) {
            link.setDescription(linkUpdateFormDto.getDescription());
        }
        if (nonNull(linkUpdateFormDto.getCategory())) {
            link.setCategory(categoryRepository.findByNameAndUser(linkUpdateFormDto.getCategory().getName(), user)
                    .orElseThrow(CategoryNotFoundException::new));
        }
        if (nonNull(linkUpdateFormDto.getStatus())) {
            link.setStatus(LinkStatus.valueOf(linkUpdateFormDto.getStatus().name()));
        }
        return mapper.mapToDto(linkRepository.save(link));

    }

    public LinkCategoryDto updateCategory(CategoryUpdateFormDto categoryUpdateFormDto, String username) {
        final var user = findAppUser(username);
        LinkCategory linkCategory = categoryRepository.findByNameAndUser(categoryUpdateFormDto.getOldName(), user)
                .orElseThrow(CategoryNotFoundException::new);
        linkCategory.setName(categoryUpdateFormDto.getNewName());
        return mapper.mapToDto(categoryRepository.save(linkCategory), false);
    }

    public void deleteLink(String linkId, String username) {
        linkRepository.delete(linkRepository.findByIdAndUser(linkId, findAppUser(username))
                .orElseThrow(LinkNotFoundException::new));
    }

    public void deleteCategory(String categoryId, String username) {
        categoryRepository.delete(categoryRepository.findByIdAndUser(categoryId, findAppUser(username))
                .orElseThrow(CategoryNotFoundException::new));
    }
}
