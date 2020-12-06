package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.javalon4.finalproject.dto.LinkDto;
import pl.javalon4.finalproject.enity.Link;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {

    Collection<Link> findByDescriptionContainingIgnoreCase(String description);

    Optional<Link> findByUrl(String url);

}
