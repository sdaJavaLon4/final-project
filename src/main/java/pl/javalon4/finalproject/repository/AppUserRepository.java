package pl.javalon4.finalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.javalon4.finalproject.enity.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByLogin(String login);

    @Modifying
    @Query("UPDATE app_user SET password=:password, email=:email WHERE login=:login ")
    int updatePasswordAndEmail(String login, String password, String email);
    @Modifying
    @Query("UPDATE app_user SET password=:password WHERE login=:login")
    int updatePassword(String login, String password);
    @Modifying
    @Query("UPDATE app_user SET email=:email WHERE login=:login")
    int updateEmail(String login, String email);
}
