package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.Link;

import java.util.Collection;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, String> {

    Collection<Link> findByDescriptionContainingIgnoreCase(String description);

    Optional<Link> findByUrl(String url);

}
