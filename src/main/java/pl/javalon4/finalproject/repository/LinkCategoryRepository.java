package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.LinkCategory;

import java.util.UUID;

public interface LinkCategoryRepository extends JpaRepository< LinkCategory, UUID> {

}
