package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.Link;
import pl.javalon4.finalproject.enity.LinkCategory;

import java.util.Optional;
import java.util.UUID;

public interface LinkCategoryRepository extends JpaRepository<LinkCategory, UUID> {

    Optional<LinkCategory> findByName(String name);
}
