package pl.javalon4.finalproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.AppUser;
import pl.javalon4.finalproject.enity.Link;

import java.util.Collection;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, String> {

    Collection<Link> findByDescriptionContainingIgnoreCaseAndUser(String description, AppUser user);

    Optional<Link> findByUrl(String url);

    Collection<Link> findByUser(AppUser user);

    Page<Link> findByUser(AppUser user, Pageable page);

    Optional<Link> findByIdAndUser(String id, AppUser user);

}
