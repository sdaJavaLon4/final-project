package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.Link;
import pl.javalon4.finalproject.enity.LinkCategory;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface LinkCategoryRepository extends JpaRepository<LinkCategory, String> {

    Optional<LinkCategory> findByNameAndUser(String name, AppUser user);

    Collection<LinkCategory> findByUser(AppUser user);

    Optional<LinkCategory> findByIdAndUser(String id, AppUser user);
}
