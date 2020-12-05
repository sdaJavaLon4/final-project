package pl.javalon4.finalproject.mapper;

import org.springframework.stereotype.Component;
import pl.javalon4.finalproject.dto.CategoryFormDto;
import pl.javalon4.finalproject.dto.LinkCategoryDto;
import pl.javalon4.finalproject.dto.LinkDto;
import pl.javalon4.finalproject.dto.LinkFormDto;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.Link;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.enity.LinkStatus;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static pl.javalon4.finalproject.enity.Link.*;

@Component
public class LinkMapper {

   public LinkDto mapToDto(Link link) {

       return new LinkDto(link.getUrl(), link.getDescription(),link.getStatus(), link.getCategory());
   }

    public LinkCategoryDto mapToDto(LinkCategory linkCategory, boolean showLinks) {
       if (showLinks) {
           return new LinkCategoryDto(linkCategory.getName());
       } else return new LinkCategoryDto(linkCategory.getName(), linkCategory.getLinks());
    }

    public Link mapToEntity(LinkFormDto linkFormDto, AppUser appUser) {

       return new Link(UUID.randomUUID().toString(), linkFormDto.getUrl(), linkFormDto.getDescription(),
               LinkStatus.TO_READ, appUser);

    }
    public LinkCategory mapToEntity(CategoryFormDto categoryFormDto) {
       return new LinkCategory(UUID.randomUUID().toString(), categoryFormDto.getName());
    }
}
