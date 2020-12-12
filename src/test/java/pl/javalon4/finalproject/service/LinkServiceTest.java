package pl.javalon4.finalproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class LinkServiceTest {

    private AppUserRepository appUserRepository;

    private LinkCategoryRepository categoryRepository;

    private LinkRepository linkRepository;

    private LinkService linkService;

    private static AppUser user;

    @BeforeAll
    public static void setupUser() {
        user = new AppUser(UUID.randomUUID().toString(), "login", "pass", "test@test.com");
    }

    @BeforeEach
    public void setup() {
        appUserRepository = mock(AppUserRepository.class);
        categoryRepository = mock(LinkCategoryRepository.class);
        linkRepository = mock(LinkRepository.class);
        LinkMapper mapper = new LinkMapper();
        linkService = new LinkService(appUserRepository, categoryRepository, linkRepository, mapper);
    }

    @Test
    public void shouldFoundAllLinksByProvidedDescriptionAndMapToDto() {
        //given
        final List<Link> links = createLinks();

        when(linkRepository.findByDescriptionContainingIgnoreCase("test")).thenReturn(links);

        //when
        Collection<LinkDto> result = linkService.search("test");

        //then
        Assertions.assertTrue(result.stream().allMatch(linkDto -> linkDto.getDescription().contains("test")));
        assertThat(result.size()).isEqualTo(2);
        verify(linkRepository).findByDescriptionContainingIgnoreCase("test");
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    public void shouldGetListOfAllLinksAndMapToDto() {
        //given
        List<Link> links = createLinks();
        List<String> actualListOfIds = links.stream().map(Link::getId).sorted().collect(Collectors.toList());
        when(linkRepository.findAll()).thenReturn(links);

        //when
        List<LinkDto> result = linkService.getAll();
        List<String> resultListOfIds = result.stream().map(LinkDto::getId).sorted().collect(Collectors.toList());

        //then
        assertThat(actualListOfIds).isEqualTo(resultListOfIds);
        assertThat(result.size()).isEqualTo(2);
        verify(linkRepository).findAll();
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    public void shouldGetAllCategoriesWithLinksAndMapToDto() {
        //given
        List<LinkCategory> categories = createCategories();
        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        List<LinkCategoryDto> result = linkService.getAllCategories(true);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().filter(linkCategoryDto -> linkCategoryDto.getName().equals("sport"))
                .mapToLong(linkCategoryDto -> linkCategoryDto.getLinks().size()).sum()).isEqualTo(2);
        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void shouldGetAllCategoriesWithoutLinksAndMapToDto() {
        //given
        List<LinkCategory> categories = createCategories();
        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        List<LinkCategoryDto> result = linkService.getAllCategories(false);

        //then
        assertThat(result.size()).isEqualTo(2);
        Assertions.assertTrue(result.stream().allMatch(linkCategoryDto -> linkCategoryDto.getLinks().isEmpty()));
        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void shouldCreateLinkAndMapToDto() {
        //given
        when(appUserRepository.findByLogin("login")).thenReturn(Optional.of(user));
        LinkCategory sportCategory = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        when(categoryRepository.findByName("sport")).thenReturn(Optional.of(sportCategory));
        Link createdLink = new Link(UUID.randomUUID().toString(), "https://www.test.pl", "test", LinkStatus.TO_READ,
                sportCategory, user);
        when(linkRepository.save(any())).thenReturn(createdLink);
        final var linkFormDto = createLinkFormDto();

        //when
        LinkDto result = linkService.createLink(linkFormDto, "login");

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getDescription()).isEqualTo(linkFormDto.getDescription());
        assertThat(result.getUrl()).isEqualTo(linkFormDto.getUrl());
        assertThat(result.getLinkCategory()).isEqualTo(linkFormDto.getLinkCategory());
        assertThat(result.getLinkStatus()).isEqualTo(LinkStatusDto.TO_READ);
        verify(appUserRepository).findByLogin("login");
        verify(categoryRepository).findByName("sport");
        verify(linkRepository).save(any());
        verifyNoMoreInteractions(appUserRepository);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenTryToCreatingLink() {
        //given
        final var linkFormDto = createLinkFormDto();
        when(appUserRepository.findByLogin("error")).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> linkService.createLink(linkFormDto, "error"));
    }

    @Test
    public void shouldThrowCategoryNotFoundExceptionWhenTryToCreatingLink() {
        //given
        final var linkFormDto = createLinkFormDto();
        when(appUserRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(categoryRepository.findByName("error")).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundException.class)
                .isThrownBy(() -> linkService.createLink(linkFormDto, "login"));
    }

    @Test
    public void shouldCreateLinkCategoryAndMapToDto() {
        //given
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        final var categoryFormDto = createCategoryFormDto();
        when(appUserRepository.findByLogin("login")).thenReturn(Optional.of(user));
        when(categoryRepository.save(any())).thenReturn(linkCategory1);

        //when
        LinkCategoryDto result = linkService.createCategory(categoryFormDto, "login");

        //then
        assertThat(result.getName()).isEqualTo(categoryFormDto.getName());
        assertThat(result.getLinks()).isEmpty();
        verify(appUserRepository).findByLogin("login");
        verify(categoryRepository).save(any());
        verifyNoMoreInteractions(appUserRepository);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void shouldThrowUserNotfoundExceptionWhenTryCreatingCategory() {
        //given
        final var categoryFormDto = createCategoryFormDto();
        when(appUserRepository.findByLogin("error")).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> linkService.createCategory(categoryFormDto, "error"));
    }

    @Test
    public void shouldUpdateAllFieldsInExistingLink() {
        //given
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        final var link1 = new Link(UUID.randomUUID().toString(), "https://www.test.pl", "test", LinkStatus.TO_READ,
                linkCategory1, user);
        String musicCategory = "music";
        final var linkCategory2 = new LinkCategory(UUID.randomUUID().toString(), musicCategory, user);
        when(linkRepository.findById(link1.getId())).thenReturn(Optional.of(link1));
        when(categoryRepository.findByName(musicCategory)).thenReturn(Optional.of(linkCategory2));
        when(linkRepository.save(link1)).thenReturn(link1);
        final var linkUpdateFormDto = createLinkUpdateFormDto(link1.getId(),
                new LinkCategoryDto(musicCategory, Collections.emptyList()));

        //when
        LinkDto result = linkService.updateLink(linkUpdateFormDto);

        //then
        assertThat(result.getId()).isEqualTo(linkUpdateFormDto.getId());
        assertThat(result.getDescription()).isEqualTo(linkUpdateFormDto.getDescription());
        assertThat(result.getUrl()).isEqualTo(linkUpdateFormDto.getUrl());
        assertThat(result.getLinkCategory()).isEqualTo(linkUpdateFormDto.getCategory());
        assertThat(result.getLinkStatus()).isEqualTo(linkUpdateFormDto.getStatus());

    }

    @Test
    public void shouldUpdateDescriptionInExistingLink() {

    }

    @Test
    public void shouldUpdateUrlInExistingLink() {

    }

    @Test
    public void shouldUpdateStatusInExistingLink() {

    }

    @Test
    public void shouldUpdateCategoryInExistingLink() {

    }

    @Test
    public void shouldThrowLinkNotFoundExceptionWhenTryToUpdateExistingLink() {
        //given
        LinkUpdateFormDto linkUpdateFormDto = createLinkUpdateFormDto("notExisting", null);
        when(linkRepository.findById(linkUpdateFormDto.getId())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(LinkNotFoundException.class)
                .isThrownBy(() -> linkService.updateLink(linkUpdateFormDto));

    }

    @Test
    public void shouldThrowCategoryNotFoundExceptionWhenTryToUpdateExistingLink() {
        //given
    }

    @Test
    public void shouldUpdateCategoryName() {
        //given
        final var categoryUpdateFormDto = createCategoryUpdateFormDto();
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        when(categoryRepository.findByName(categoryUpdateFormDto.getOldName())).thenReturn(Optional.of(linkCategory1));
        when(categoryRepository.save(linkCategory1)).thenReturn(linkCategory1);

        //when
        LinkCategoryDto linkCategoryDto = linkService.updateCategory(categoryUpdateFormDto);

        //then
        assertThat(linkCategoryDto.getName()).isEqualTo(categoryUpdateFormDto.getNewName());
    }

    @Test
    public void shouldThrowCategoryNotFoundExceptionWhenTryToUpdateExistingCategory() {
        //given
        final var categoryUpdateFormDto = createCategoryUpdateFormDto();
        when(categoryRepository.findByName("error")).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundException.class)
                .isThrownBy(() -> linkService.updateCategory(categoryUpdateFormDto));
    }

    @Test
    public void shouldDeleteExistingLink() {
        //given
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        final var link1 = new Link(UUID.randomUUID().toString(), "https://www.test.pl", "test", LinkStatus.TO_READ,
                linkCategory1, user);
        when(linkRepository.findById(link1.getId())).thenReturn(Optional.of(link1));
        doNothing().when(linkRepository).delete(link1);

        //when
        linkService.deleteLink(link1.getId());

        //then
        verify(linkRepository).findById(link1.getId());
        verify(linkRepository).delete(link1);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    public void shouldThrowLinkNotFoundExceptionWhenTryToDeleteExistingLink() {
        //given
        when(linkRepository.findById("error")).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(LinkNotFoundException.class).isThrownBy(() -> linkService.deleteLink("error"));
    }

    @Test
    public void shouldDeleteExistingCategory() {
        //given
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(linkCategory1));
        doNothing().when(categoryRepository).delete(linkCategory1);

        //when
        linkService.deleteCategory(any());

        //then
        verify(categoryRepository).findById(any());
        verify(categoryRepository).delete(linkCategory1);
    }

    @Test
    public void shouldThrowCategoryNotFoundExceptionWhenTryToDeleteExistingCategory() {
        //given
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(CategoryNotFoundException.class).isThrownBy(() -> linkService.deleteCategory(any()));
    }

    private List<Link> createLinks() {
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        final var link1 = new Link(UUID.randomUUID().toString(), "https://www.test.pl", "test", LinkStatus.TO_READ,
                linkCategory1, user);
        final var link2 = new Link(UUID.randomUUID().toString(), "https://www.test2.pl", "test2", LinkStatus.TO_READ,
                linkCategory1, user);
        return List.of(link1, link2);
    }

    private LinkFormDto createLinkFormDto() {
        LinkCategoryDto linkCategoryDto = new LinkCategoryDto("sport", Collections.emptyList());
        return new LinkFormDto("https://www.test.pl", "test", linkCategoryDto);
    }

    private List<LinkCategory> createCategories() {
        final var linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        final var linkCategory2 = new LinkCategory(UUID.randomUUID().toString(), "cooking", user);
        linkCategory1.setLinks(createLinks());
        return List.of(linkCategory1, linkCategory2);
    }

    private CategoryFormDto createCategoryFormDto() {
        return new CategoryFormDto("sport");
    }

    private LinkUpdateFormDto createLinkUpdateFormDto(String linkId, LinkCategoryDto linkCategoryDto) {
        return new LinkUpdateFormDto(linkId, "https://www.newurl.pl", "new description", LinkStatusDto.ARCHIVED,
                linkCategoryDto);
    }

    private CategoryUpdateFormDto createCategoryUpdateFormDto() {
        return new CategoryUpdateFormDto("sport", "fitness");
    }
}
