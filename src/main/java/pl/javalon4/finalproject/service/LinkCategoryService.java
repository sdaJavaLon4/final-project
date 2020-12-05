package pl.javalon4.finalproject.service;

import org.springframework.stereotype.Service;
import pl.javalon4.finalproject.dto.LinkCategoryDTO;
import pl.javalon4.finalproject.dto.UserForm;
import pl.javalon4.finalproject.enity.LinkCategory;
import pl.javalon4.finalproject.repository.LinkCategoryRepository;

import java.util.Collections;
import java.util.UUID;

@Service
public class LinkCategoryService {

    private  final LinkCategoryRepository repozitory;

    public LinkCategoryService(LinkCategoryRepository linkCategoryRepository) {
        this.repozitory = linkCategoryRepository;
    }


    public void addCategory(LinkCategoryDTO category) {
        repozitory.save(new LinkCategory(UUID.randomUUID().toString(),category.getName(), Collections.emptyList()));

    }
}
