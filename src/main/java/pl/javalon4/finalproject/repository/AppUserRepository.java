package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javalon4.finalproject.enity.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByLogin(String login);


}
