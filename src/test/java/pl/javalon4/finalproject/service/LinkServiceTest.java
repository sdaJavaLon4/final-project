package pl.javalon4.finalproject.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.javalon4.finalproject.dto.LinkCategoryDto;
import pl.javalon4.finalproject.dto.LinkDto;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.Link;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.enity.LinkStatus;
import pl.javalon4.finalproject.mapper.LinkMapper;
import pl.javalon4.finalproject.repository.AppUserRepository;
import pl.javalon4.finalproject.repository.LinkCategoryRepository;
import pl.javalon4.finalproject.repository.LinkRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LinkServiceTest {

    private AppUserRepository appUserRepository;

    private LinkCategoryRepository categoryRepository;

    private LinkRepository linkRepository;

    private LinkMapper mapper;

    private LinkService linkService;

    private AppUser user;
    private LinkCategory linkCategory1;
    private LinkCategory linkCategory2;
    private Link link1;
    private Link link2;
    private Collection<Link> links;

    @BeforeEach
    public void setup() {
        appUserRepository = mock(AppUserRepository.class);
        categoryRepository = mock(LinkCategoryRepository.class);
        linkRepository = mock(LinkRepository.class);
        mapper = new LinkMapper();
        linkService = new LinkService(appUserRepository, categoryRepository, linkRepository, mapper);
        user = new AppUser(UUID.randomUUID().toString(), "login", "pass", "test@test.com");
        linkCategory1 = new LinkCategory(UUID.randomUUID().toString(), "sport", user);
        linkCategory2 = new LinkCategory(UUID.randomUUID().toString(), "music", user);
        link1 = new Link(UUID.randomUUID().toString(), "https://www.test.pl", "test", LinkStatus.TO_READ, linkCategory1, user);
        link2 = new Link(UUID.randomUUID().toString(), "https://www.test2.pl", "test2", LinkStatus.TO_READ, linkCategory1, user);
        links = List.of(link1, link2);
        linkCategory1.setLinks((List<Link>) links);
    }
    @Test
    public void shouldFoundAllLinksByProvidedDescriptionAndMapToDto () {
        //given

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
        List<Link> links = List.of(link1, link2);
        List<String> actualListOfIds = links.stream().map(Link::getId).sorted().collect(Collectors.toList());
        when(linkRepository.findAll()).thenReturn(links);

        //when
        List<LinkDto> result = linkService.getAll();
        List<String> resultListOfIds = result.stream().map(LinkDto::getId).collect(Collectors.toList());

        //then
        assertThat(actualListOfIds).isEqualTo(resultListOfIds);
        assertThat(result.size()).isEqualTo(2);
        verify(linkRepository).findAll();
        verifyNoMoreInteractions(linkRepository);
    }
    @Test
    public void shouldGetAllCategoriesWithLinksAndMapToDto() {
        //given
        List<LinkCategory> categories = List.of(linkCategory1, linkCategory2);
        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        List<LinkCategoryDto> result = linkService.getAllCategories(true);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream()
                .filter(linkCategoryDto -> linkCategoryDto.getName().equals("sport"))
                .mapToLong(linkCategoryDto -> linkCategoryDto.getLinks().size()).sum())
                .isEqualTo(2);
    }

}